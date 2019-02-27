package com.lance.api.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回编码
 *
 * @author lance
 * @since 2018-10-17
 */

public enum ResponseCode
{
    //成功代码
    _9999("9999", "交易成功"),
    //程序类错误
    /**
     * 报文的格式和标准的报文格式不一致。
     */
    _0000("0000", "报文格式错误"),
    /**
     * 销账机构处理的时候出现异常,不会退钱
     */
    _0009("0009", "系统异常"),
    /**
     * 查询不合法
     */
    _1002("1002", "查询不合法");


    private String key;
    private String value;
    private Map<String, String> valuesMap = new HashMap<>();
    private String code;

    public String getValue(String s)
    {
        return valuesMap.get(s);
    }

    public static class Values
    {


        public Values(String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        private String key;
        private String value;

        public String getKey()
        {
            return key;
        }

        public void setKey(String key)
        {
            this.key = key;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

    }

    private ResponseCode(String code, Values... values)
    {
        this.code = code;
        if (values != null && values.length > 0)
        {
            this.code = code;
            this.key = values[0].getKey();
            this.value = values[0].getValue();

            for (Values v : values)
            {
                valuesMap.put(v.getKey(), v.getValue());
            }
        }
    }

    private ResponseCode(String key, String value)
    {
        this.code = key;
        this.key = key;
        this.value = value;
    }


    public String getCode()
    {
        return code;
    }

    public String getKey()
    {
        return this.key;
    }

    public String getValue()
    {
        return this.value;
    }

    @Override
    public String toString()
    {
        return this.key + ":" + this.value;
    }
}
