package com.lance.api.common.base.model;


import com.lance.api.common.constant.ResponseCode;

import java.util.HashMap;
import java.util.Map;

public class Communication
{
    private static Communication communication = null;

    public static Communication getInstance()
    {
        if (communication == null)
        {
            communication = new Communication();
        }
        return communication;
    }


    public Map<String, Object> getSuccessMap()
    {
        Map<String, Object> res = new HashMap<>(3);
        res.put("rtnCode", ResponseCode._9999.getKey());
        res.put("rtnMsg", ResponseCode._9999.getValue());
        res.put("extend", "");
        return res;
    }

    public Map<String, Object> getErroMap()
    {
        Map<String, Object> res = new HashMap<>(3);
        res.put("rtnCode", ResponseCode._0009.getKey());
        res.put("rtnMsg", ResponseCode._0009.getValue());
        res.put("extend", "");
        return res;
    }


    public Map<String, Object> getFailedMap(String rtnCode, String rtnMsg)
    {
        Map<String, Object> res = new HashMap<>(3);
        res.put("rtnCode", rtnCode);
        res.put("rtnMsg", rtnMsg);
        res.put("extend", "");
        return res;
    }


    public Map<String, Object> getFailedMap(String rtnCode, String rtnMsg, String extend)
    {
        Map<String, Object> res = new HashMap<>(3);
        res.put("rtnCode", rtnCode);
        res.put("rtnMsg", rtnMsg);
        res.put("extend", extend);
        return res;
    }
}
