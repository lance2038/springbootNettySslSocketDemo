package com.lance.api.business.dao;

import com.lance.api.common.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public class SysDao extends BaseDao
{
    /**
     * 获取当前系统时间
     *
     * @return
     */
    public String getDBCurrentTime()
    {
        String sql = "select to_char(sysdate,'yyyy-MM-dd hh24:mi:ss') from dual";
        return queryForString(sql);
    }

    /**
     * 获取当前系统日期
     *
     * @return
     */
    public String getDBCurrentDate()
    {
        String sql = "select to_char(sysdate,'yyyy-MM-dd') from dual";
        return queryForString(sql);
    }
}
