package com.lance.api.business.service.impl;


import com.lance.api.business.dao.ApiDao;
import com.lance.api.business.dao.SysDao;
import com.lance.api.business.pojo.dto.SampleModel;
import com.lance.api.business.service.ApiService;
import com.lance.api.common.base.model.JsonResult;
import com.lance.api.common.constant.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>业务处理service接口实现类
 *
 * @author lance
 * @since 2018-10-15
 **/
@Slf4j
@Service
public class ApiServiceImpl implements ApiService
{
    @Autowired
    private SysDao sysDao;
    @Autowired
    private ApiDao apiDao;

    /**
     * 获取信息
     *
     * @param key 标识符
     * @return 查询结果
     * @throws Exception
     */
    @Override
    public JsonResult queryInfo(String key) throws Exception
    {
        if (key.contains("1"))
        {
            return new JsonResult(false, "查询失败", ResponseCode._1002);
        }
        return new JsonResult(true, "查询成功", new SampleModel(65535, BigDecimal.TEN));
    }

    /**
     * 保存信息
     *
     * @param key 标识
     * @param msg 信息
     * @return 存储结果
     * @throws Exception
     */
    @Override
    public JsonResult saveData(String key, String msg) throws Exception
    {
        System.out.println(key);
        System.out.println(msg);
        return new JsonResult(true, "存储成功");
    }
}
