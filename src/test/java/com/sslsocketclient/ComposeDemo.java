package com.sslsocketclient;


import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 报文组装
 */
public class ComposeDemo
{

    protected byte[] doCanProcess(Map paraMap, String serverCode) throws Exception
    {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> map = new HashMap();
        Map<String, Object> headMap = new HashMap();
        // 服务编码
        String servCode = serverCode;
        headMap.put("version", "1.0.0");
        // 服务编码
        headMap.put("servCode", servCode);
        // msgId 32位不可重复的流水

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        Date msgtime = new Date();
        headMap.put("msgId", "test" + sdf.format(msgtime));

        headMap.put("msgTime", sdf.format(msgtime));
        headMap.put("extend", "");
        map.put("head", headMap);
        map.put("body", paraMap);

        String message = jsonObject.toJSONString(map).replace("\\", "");

        System.out.println("请求的报文为：" + message);
        ByteDataBuffer bdb = new ByteDataBuffer();
        bdb.setInBigEndian(false);
        // 开始字节
        bdb.writeInt8((byte) 0x68);
        int len = Integer.parseInt(getLen(message, 4));
        // 用于计算数据域的长度是否大于4字节
        // 报文长度
        bdb.writeInt32(len);
        // 服务编码
        bdb.writeString(servCode, 6);
        // 报文frame
        bdb.writeBytes(message.getBytes());
        // 结束字节
        bdb.writeInt8((byte) 0x16);
        return bdb.getBytes();
    }


    /**
     * 计算报文长度，不足四位左补零
     *
     * @param text    报文信息
     * @param needlen 报文长度规定的字符数
     * @return
     */
    public static String getLen(String text, int needlen)
    {
        if (text != null)
        {
            int len;
            try
            {
                len = text.getBytes("utf-8").length;
                String lenStr = String.valueOf(len);
                StringBuffer sb = new StringBuffer(lenStr);
                while (sb.length() < needlen)
                {
                    sb.insert(0, "0");

                }
                return sb.toString();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

}
