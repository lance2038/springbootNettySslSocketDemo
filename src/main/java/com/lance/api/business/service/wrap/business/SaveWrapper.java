package com.lance.api.business.service.wrap.business;


import com.lance.api.business.pojo.model.EntryBodyModel;
import com.lance.api.business.pojo.model.EntryModel;
import com.lance.api.business.service.ApiService;
import com.lance.api.business.service.wrap.core.BaseServiceWrapper;
import com.lance.api.common.base.model.Communication;
import com.lance.api.common.base.model.JsonResult;
import com.lance.api.common.constant.ResponseCode;
import com.lance.api.common.constant.ServerCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 存储包装类
 *
 * @author lance
 */
@Slf4j
@Component
public class SaveWrapper extends BaseServiceWrapper
{
    /**
     * 此业务包装类的servCode
     */
    private final String servCode = ServerCode.SERVER_CODE_SAVE;
    @Autowired
    private ApiService apiService;

    @Override
    public String doCode()
    {
        return servCode;
    }

    @Override
    public Map handle(Object... params) throws Exception
    {
        Map<String, Object> resultMap;
        ResponseCode failedMsg = null;

        EntryModel entryModel = (EntryModel) params[0];
        EntryBodyModel bodyModel = entryModel.getBodyModel();

        String key = bodyModel.getQueryValue();
        String type = bodyModel.getQueryValue();

        JsonResult saveResult = null;
        try
        {
            saveResult = apiService.saveData(key, type);
        }
        catch (Exception e)
        {
            log.error("系统发生异常: {}", e.getMessage());
            e.printStackTrace();

            return Communication.getInstance().getErroMap();
        }

        if (saveResult.getSuccess())
        {
            resultMap = Communication.getInstance().getSuccessMap();
        }
        else
        {
            failedMsg = (ResponseCode) saveResult.getData();
            resultMap = Communication.getInstance().getFailedMap(failedMsg.getKey(), failedMsg.getValue());
        }
        return resultMap;
    }
}
