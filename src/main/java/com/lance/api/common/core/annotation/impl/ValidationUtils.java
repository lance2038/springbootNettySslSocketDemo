package com.lance.api.common.core.annotation.impl;


import com.lance.api.common.core.annotation.Validation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author lance
 * 用于参数校验
 */
@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public final class ValidationUtils
{
    /**
     * 参数校验.
     *
     * @param obj pojo对象
     * @return 校验结果
     * @throws Exception e
     */
    public static String validate(Object obj)
    {
        Assert.notNull(obj, "参数为null");
        // 校验结果
        String validateResult = null;
        // 获取类字段
        Field[] fields = obj.getClass().getDeclaredFields();
        // 遍历pojo的字段进行校验
        for (Field field : fields)
        {
            try
            {
                validateResult = fieldValidate(obj, field);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
                validateResult = "校验过程出现异常";
            }
            if (!StringUtils.isEmpty(validateResult))
            {
                return validateResult;
            }
        }
        return validateResult;
    }


    /**
     * 遍历字段上的注解对指定注解进行校验.
     *
     * @param obj   参数
     * @param field 属性
     * @return 单个字段的检验结果
     * @throws Exception e
     */
    private static String fieldValidate(Object obj, Field field) throws IllegalAccessException
    {
        String backVal = null;
        // 获取字段名称
        String fieldName = field.getName();
        // 获取字段的值
        Object value = field.get(obj);
        // 获取字段上的注解
        Annotation[] annotations = field.getAnnotations();
        // 校验结果
        field.setAccessible(true);

        for (Annotation an : annotations)
        {
            // 若扫描到Verification注解
            if (an.annotationType().getName().equals(Validation.class.getName()))
            {
                // 获取指定类型注解
                Validation column = field.getAnnotation(Validation.class);
                // 进行业务处理，校验
                backVal = businessValidate(column, value);
                if (!StringUtils.isEmpty(backVal))
                {
                    return backVal;
                }
            }
        }
        field.setAccessible(false);
        return backVal;
    }

    /**
     * 业务校验
     *
     * @param column 注解
     * @param value  字段值
     * @return 业务校验结果
     */
    private static String businessValidate(Validation column, Object value)
    {
        String emptyVal;
        String limitVal;
        if (!StringUtils.isEmpty(emptyVal = mustEmptyValidate(column, value)))
        {
            return emptyVal;
        }
        else if (!StringUtils.isEmpty(limitVal = limitValueValidate(column, value)))
        {
            return limitVal;
        }
        else
        {
            return allowValueValidate(column, value);
        }
    }

    /**
     * 必须为空校验
     *
     * @param column 注解
     * @param value  字段值
     * @return 业务校验结果
     */
    private static String mustEmptyValidate(Validation column, Object value)
    {
        if (column.mustEmpty() && null != value)
        {
            return column.limitMsg();
        }
        else
        {
            return null;
        }
    }

    /**
     * 限制值校验
     *
     * @param column 注解
     * @param value  字段值
     * @return 业务校验结果
     */
    private static String limitValueValidate(Validation column, Object value)
    {
        String[] limitArray = column.limitValue();
        if (limitArray.length > 0)
        {
            value = String.valueOf(value);
            boolean result = Arrays.asList(limitArray).contains(value);
            if (result)
            {
                return column.limitMsg();
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * 允许值校验
     *
     * @param column 注解
     * @param value  字段值
     * @return 业务校验结果
     */
    private static String allowValueValidate(Validation column, Object value)
    {
        String[] allowArray = column.allowValue();
        if (allowArray.length > 0)
        {
            value = String.valueOf(value);
            boolean result = Arrays.asList(allowArray).contains(value);
            if (!result)
            {
                return column.limitMsg();
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
}

