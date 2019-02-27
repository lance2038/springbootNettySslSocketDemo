package com.lance.api.common.base.model;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

/**
 * 返回json结果封装类
 *
 * @author lance
 * @since 2017-11-24
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class JsonResult extends HashMap<String, Object>
{
    /**
     * 是否成功
     */
    private final String success = "success";

    /**
     * 返回信息
     */
    private final String message = "message";

    /**
     * 返回数据
     */
    private final String data = "data";

    public JsonResult()
    {
        super();
        this.setSuccess(true);
    }

    public JsonResult(Object data)
    {
        super();
        this.setSuccess(true);
        this.setData(data);
    }

    public JsonResult(boolean success, String message)
    {
        super();
        this.setSuccess(success);
        this.setMessage(message);
    }

    public JsonResult(boolean success, String message, Object data)
    {
        super();
        this.setSuccess(success);
        this.setMessage(message);
        this.setData(data);
    }

    public boolean getSuccess()
    {
        return (Boolean) this.get(success);
    }

    public JsonResult setSuccess(boolean value)
    {
        this.put(success, value);
        return this;
    }

    public String getMessage()
    {
        return (String) this.get(message);
    }

    public JsonResult setMessage(String value)
    {
        this.put(message, value);
        return this;
    }

    public Object getData()
    {
        return this.get(data);
    }

    public JsonResult setData(Object value)
    {
        this.put(data, value);
        return this;
    }
}
