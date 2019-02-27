package com.lance.api.business.service.wrap.core;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>service包裝类容器类
 *
 * @author lance
 * @since 2018-10-15
 */
public class ServiceWrapperContainer
{

    List<BaseServiceWrapper> services = new ArrayList<>();

    /**
     * 添加包装类
     *
     * @param baseServiceWrapper
     */
    public void addService(BaseServiceWrapper baseServiceWrapper)
    {
        services.add(baseServiceWrapper);
    }

    /**
     * 根据servCode获取service包装类
     *
     * @param servCode
     * @return
     */
    public BaseServiceWrapper getService(String servCode)
    {
        for (BaseServiceWrapper baseServiceWrapper : services)
        {
            if (baseServiceWrapper.isMyDo(servCode))
            {
                return baseServiceWrapper;
            }
        }
        return null;
    }

    /**
     * 根据servCode获取所有service包装类
     *
     * @param servCode
     * @return
     */
    public List<BaseServiceWrapper> findSameService(String servCode)
    {

        List<BaseServiceWrapper> baseServiceWrapperList = new ArrayList<>();
        for (BaseServiceWrapper baseServiceWrapper : services)
        {
            if (baseServiceWrapper.hasSameService() && baseServiceWrapper.isMyDo(servCode))
            {
                baseServiceWrapperList.add(baseServiceWrapper);
            }
        }
        return baseServiceWrapperList;
    }
}
