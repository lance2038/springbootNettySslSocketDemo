package com.lance.api.common.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringContext工具类
 *
 * @author lance
 * @version 2018-4-18
 */
@Component
public class SpringContextUtil implements ApplicationContextAware
{

    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    public static Object getBean(String name) throws BeansException
    {
        return applicationContext.getBean(name);
    }


    public static Object getBean(String name, Class requiredType) throws BeansException
    {
        return applicationContext.getBean(name, requiredType);
    }


    public static boolean containsBean(String name)
    {
        return applicationContext.containsBean(name);
    }


    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException
    {
        return applicationContext.isSingleton(name);
    }


    public static Class getType(String name) throws NoSuchBeanDefinitionException
    {
        return applicationContext.getType(name);
    }


    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException
    {
        return applicationContext.getAliases(name);
    }
}

