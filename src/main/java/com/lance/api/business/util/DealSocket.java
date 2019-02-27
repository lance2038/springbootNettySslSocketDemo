package com.lance.api.business.util;


import com.lance.api.business.pojo.model.ReturnModel;
import com.lance.api.business.pojo.model.EntryModel;
import com.lance.api.business.service.wrap.core.BaseServiceWrapper;
import com.lance.api.business.service.wrap.core.ServiceWrapperContainer;
import com.lance.api.common.base.model.Communication;
import com.lance.api.common.constant.ResponseCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedOutputStream;
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
public class DealSocket
{
    @Autowired
    private ComposeUtil composeUtil;
    /**
     * service包装类容器
     */
    @Autowired
    private ServiceWrapperContainer serviceWrapperContainer;

    /**
     * 处理业务(原生socket使用)
     *
     * @param servCode
     * @param msgId
     * @param entryModel
     * @param bos
     */
    @Async
    @SuppressWarnings("unchecked")
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
                log.info("业务处理开始: servCode [{}] msgId [{}]", servCode, msgId);

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
                log.info("业务处理完毕: servCode [{}] msgId [{}]", servCode, msgId);
            }
        }
        catch (Exception e)
        {
            log.error("业务处理发生异常: servCode [{}] msgId [{}] err \n{}", servCode, msgId, e.getMessage());
            e.printStackTrace();
            resultMap = Communication.getInstance().getFailedMap(ResponseCode._0009.getKey(), ResponseCode._0009.getValue());
        }
        finally
        {
            try
            {
                bos.write(composeUtil.doCanProcess(resultMap, servCode, msgId));
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
            log.info("业务处理开始: servCode [{}] msgId [{}]", servCode, msgId);

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
            log.info("业务处理完毕: servCode [{}] msgId [{}]", servCode, msgId);
        }
        catch (Exception e)
        {
            log.error("业务处理发生异常: servCode [{}] msgId [{}] err \n{}", servCode, msgId, e.getMessage());
            e.printStackTrace();
            resultModel.setMap(Communication.getInstance().getFailedMap(ResponseCode._0009.getKey(), ResponseCode._0009.getValue()));
        }
        return resultModel;
    }
}
