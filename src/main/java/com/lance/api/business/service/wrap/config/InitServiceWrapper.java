package com.lance.api.business.service.wrap.config;

import com.lance.api.business.service.wrap.business.CheckWrapper;
import com.lance.api.business.service.wrap.business.InquiryWrapper;
import com.lance.api.business.service.wrap.business.SaveWrapper;
import com.lance.api.business.service.wrap.core.ServiceWrapperContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>加载wrappers
 *
 * @author lance
 * @since 2018-10-15
 */
@Configuration
public class InitServiceWrapper
{
    @Bean("serviceWrapperContainer")
    public ServiceWrapperContainer initWrapper(InquiryWrapper inquiryWrapper, SaveWrapper saveWrapper, CheckWrapper checkWrapper)
    {
        ServiceWrapperContainer serviceWrapper = new ServiceWrapperContainer();
        serviceWrapper.addService(inquiryWrapper);
        serviceWrapper.addService(saveWrapper);
        serviceWrapper.addService(checkWrapper);
        return serviceWrapper;
    }
}
