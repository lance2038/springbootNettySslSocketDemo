package com.lance.api.business.util;


import com.lance.api.business.pojo.model.EntryModel;
import com.lance.api.business.pojo.model.ReturnModel;
import com.lance.api.business.service.wrap.core.BaseServiceWrapper;
import com.lance.api.business.service.wrap.core.ServiceWrapperContainer;
import com.lance.api.common.base.model.Communication;
import com.lance.api.common.constant.ResponseCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * <p>socket业务分发类
 *
 * @author lance
 * @since 2018-10-15
 */
@Data
@Slf4j
@Component
@SuppressWarnings("unchecked")
public class DealSocket
{
    /**
     * service包装类容器
     */
    @Autowired
    private ServiceWrapperContainer serviceWrapperContainer;

    /**
     * 解析并处理报文
     *
     * @param socket
     * @throws Exception
     */
    @Async
    public void analysisAndDealMsg(Socket socket) throws Exception
    {
        /*
          1.获取socket数据
        */
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        BufferedInputStream bis = new BufferedInputStream(input);
        BufferedOutputStream bos = new BufferedOutputStream(output);
        byte[] buffer = new byte[36000];
        bis.read(buffer);

        // 解析报文
        EntryModel entryModel = ComposeUtil.deSplit(new ByteDataBuffer(buffer));
        if (entryModel == null)
        {
            noSignFlag(bos);
            return;
        }
        // 业务请求码
        String servCode = entryModel.getHeadModel().getServCode();
        // 报文标识
        String msgId = entryModel.getHeadModel().getMsgId();
        /*
          2.业务处理
        */
        MDC.put("logId", msgId);
        doBusiness(servCode, msgId, entryModel, bos);
        MDC.clear();

    }

    /**
     * 处理业务(原生socket使用)
     *
     * @param servCode
     * @param msgId
     * @param entryModel
     * @param bos
     */
    @Async
    public void doBusiness(String servCode, String msgId, EntryModel entryModel, BufferedOutputStream bos)
    {
        Map<String, Object> resultMap = null;
        try
        {
            if (StringUtils.isEmpty(servCode) || StringUtils.isEmpty(msgId))
            {
                resultMap = Communication.getInstance().getFailedMap(ResponseCode._0000.getKey(), ResponseCode._0000.getValue());
            }
            else
            {
                log.info("业务处理开始");

                // 根据servCode查询所对应的service业务处理类
                BaseServiceWrapper baseServiceWrapper = serviceWrapperContainer.getService(servCode);
                // 或有业务存在servCode相同的情况，需加以判断是否存在servCode相同的情况
                if (baseServiceWrapper.hasSameService())
                {
                    // 根据servCode查询出所有servCode相同的service
                    List<BaseServiceWrapper> baseServiceWrappers = serviceWrapperContainer.findSameService(servCode);
                    for (BaseServiceWrapper bs : baseServiceWrappers)
                    {
                        if (bs.sameIsMyDo(entryModel))
                        {
                            resultMap = bs.handle(entryModel);
                        }
                    }
                }
                else
                {
                    resultMap = baseServiceWrapper.handle(entryModel);
                }
                log.info("业务处理完毕");
            }
        }
        catch (Exception e)
        {
            log.error("业务处理发生异常:\n{}", e.getMessage());
            e.printStackTrace();
            resultMap = Communication.getInstance().getFailedMap(ResponseCode._0009.getKey(), ResponseCode._0009.getValue());
        }
        finally
        {
            try
            {
                bos.write(ComposeUtil.doCanProcess(resultMap, servCode, msgId));
                bos.flush();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理业务(netty使用)
     *
     * @param servCode
     * @param msgId
     * @param entryModel
     */
    public ReturnModel doBusiness(String servCode, String msgId, EntryModel entryModel)
    {
        ReturnModel resultModel = new ReturnModel();
        resultModel.setServCode(servCode);
        resultModel.setMsgId(msgId);
        try
        {
            if (StringUtils.isEmpty(servCode) || StringUtils.isEmpty(msgId))
            {
                return resultModel.setMap(Communication.getInstance().getFailedMap(ResponseCode._0000.getKey(), ResponseCode._0000.getValue()));
            }
            log.info("业务处理开始");

            // 根据servCode查询所对应的service业务处理类
            BaseServiceWrapper baseServiceWrapper = serviceWrapperContainer.getService(servCode);
            // 或有业务存在servCode相同的情况，需加以判断是否存在servCode相同的情况
            if (baseServiceWrapper.hasSameService())
            {
                // 根据servCode查询出所有servCode相同的service
                List<BaseServiceWrapper> baseServiceWrappers = serviceWrapperContainer.findSameService(servCode);
                for (BaseServiceWrapper bs : baseServiceWrappers)
                {
                    if (bs.sameIsMyDo(entryModel))
                    {
                        resultModel.setMap(bs.handle(entryModel));
                    }
                }
            }
            else
            {
                resultModel.setMap(baseServiceWrapper.handle(entryModel));
            }
            log.info("业务处理完毕");
        }
        catch (Exception e)
        {
            log.error("业务处理发生异常:\n{}", e.getMessage());
            e.printStackTrace();
            resultModel.setMap(Communication.getInstance().getFailedMap(ResponseCode._0009.getKey(), ResponseCode._0009.getValue()));
        }
        return resultModel;
    }

    /**
     * 若无起始标记,则返回无标识信息
     *
     * @param bos
     */
    public void noSignFlag(BufferedOutputStream bos)
    {
        Map<String, Object> resultMap = Communication.getInstance().getFailedMap(ResponseCode._0000.getKey(), ResponseCode._0000.getValue());
        try
        {
            bos.write(ComposeUtil.doCanProcess(resultMap, "", ""));
            bos.flush();
            bos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
