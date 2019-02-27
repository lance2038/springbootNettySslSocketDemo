package com.lance.api.business.service.wrap.business;

import com.lance.api.business.pojo.dto.SampleModel;
import com.lance.api.business.pojo.model.EntryBodyModel;
import com.lance.api.business.pojo.model.EntryModel;
import com.lance.api.business.pojo.model.FieldNameModel;
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
 * 查询包装类
 *
 * @author lance
 * @since 2018-10-15
 */
@Slf4j
@Component
public class InquiryWrapper extends BaseServiceWrapper
{
    /**
     * 此业务包装类的servCode
     */
    private final String servCode = ServerCode.SERVER_CODE_QUERY;
    @Autowired
    private ApiService apiService;

    @Override
    public String doCode()
    {
        return servCode;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map handle(Object... params) throws Exception
    {
        // 返回结果map
        Map<String, Object> resultMap;
        ResponseCode failedMsg = null;

        // 获取传入参数
        EntryModel entryModel = (EntryModel) params[0];
        EntryBodyModel bodyModel = entryModel.getBodyModel();
        // 获取查询标识
        String queryValue = bodyModel.getQueryValue();

        // 查询查询信息
        JsonResult queryInfo = apiService.queryInfo(queryValue);

        // 如果获取成功
        if (queryInfo.getSuccess())
        {
            SampleModel model = (SampleModel) queryInfo.getData();
            resultMap = Communication.getInstance().getSuccessMap();
            resultMap.put(FieldNameModel.RECORD_COUNT, model.getCount());
            resultMap.put(FieldNameModel.RECORD_AMT, model.getAmt());
        }
        else
        {
            failedMsg = (ResponseCode) queryInfo.getData();
            resultMap = Communication.getInstance().getFailedMap(failedMsg.getKey(), failedMsg.getValue());
        }
        return resultMap;
    }
}
