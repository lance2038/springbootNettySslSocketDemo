package com.lance.api.business.service.wrap.core;

import java.util.Map;

/**
 * <p>包装类基类
 *
 * @author lance
 * @since 2018-10-15
 */
public abstract class BaseServiceWrapper extends SameServiceWrapper
{
    /**
     * 获取当前servCode
     *
     * @return
     */
    public abstract String doCode();

    /**
     * 传入的servCode是否为当前包装类的servCode
     *
     * @param doCode
     * @return
     */
    public Boolean isMyDo(String doCode)
    {
        return doCode.equals(doCode());
    }

    /**
     * 处理业务
     *
     * @param params
     * @return
     * @throws Exception
     */
    public abstract Map handle(Object... params) throws Exception;
}
