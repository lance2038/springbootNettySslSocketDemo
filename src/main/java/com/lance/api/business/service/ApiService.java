package com.lance.api.business.service;


import com.lance.api.common.base.model.JsonResult;

/**
 * <p>业务处理service接口
 *
 * @author lance
 * @since 2018-10-15
 **/
public interface ApiService
{
    /**
     * 获取信息
     *
     * @param key 标识符
     * @return 查询结果
     * @throws Exception
     */
    JsonResult queryInfo(String key) throws Exception;


    /**
     * 保存信息
     *
     * @param key 标识
     * @param msg 信息
     * @return 存储结果
     * @throws Exception
     */
    JsonResult saveData(String key, String msg) throws Exception;
}
