package com.lance.api.business.service.wrap.business;

import com.lance.api.business.service.wrap.core.BaseServiceWrapper;
import com.lance.api.common.base.model.Communication;
import com.lance.api.common.constant.ServerCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 校验包装类
 *
 * @author lance
 */
@Slf4j
@Component
public class CheckWrapper extends BaseServiceWrapper
{

    /**
     * 此业务包装类的servCode
     */
    private final String servCode = ServerCode.SERVER_CODE_CHECK;

    /**
     * 获取当前servCode
     *
     * @return
     */
    @Override
    public String doCode()
    {
        return servCode;
    }

    /**
     * 处理业务--校验
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public Map handle(Object... params) throws Exception
    {
        // 返回结果map
        return Communication.getInstance().getSuccessMap();
    }
}
