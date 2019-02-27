package com.lance.api.common.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface Validation
{
    // 允许的值
    String[] allowValue() default {};

    // 限制的值
    String[] limitValue() default {};

    // 必须为空
    boolean mustEmpty() default false;

    // 若值为限制值的返回信息
    String limitMsg() default "校验未通过";

}