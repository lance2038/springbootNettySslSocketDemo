package com.lance.api.business.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lance.api.business.pojo.model.EntryBodyModel;
import com.lance.api.business.pojo.model.EntryHeadModel;
import com.lance.api.business.pojo.model.EntryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>报文组装解析类
 *
 * @author lance
 * @since 2018-10-11
 */
@Slf4j
@Component
public class ComposeUtil
{
    private static final byte BLOCK_BGN_SIGN = 0x68;
    private static final byte BLOCK_END_SIGN = 0x16;
    private static final String ENCODING = "UTF-8";

    /**
     * 组装报文
     *
     * @param paraMap
     * @param serverCode
     * @param msgId
     * @return
     * @throws Exception
     */
    public static byte[] doCanProcess(Map paraMap, String serverCode, String msgId) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> headMap = new HashMap<>();
        // 服务编码
        String servCode = serverCode;
        headMap.put("version", "1.0.0");
        // 服务编码
        headMap.put("servCode", servCode);

        // msgId 32位不可重复的流水
        headMap.put("msgId", msgId);
        headMap.put("msgTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        headMap.put("extend", "");
        map.put("head", headMap);
        map.put("body", paraMap);
        String message = JSONObject.toJSONString(map).replace("\\", "");

        log.info("返回的报文为:\n{}", message);
        ByteDataBuffer bdb = new ByteDataBuffer();
        bdb.setInBigEndian(false);
        // 开始字节
        bdb.writeInt8((byte) BLOCK_BGN_SIGN);
        int len = Integer.parseInt(getLen(message, 4));
        // 用于计算数据域的长度是否大于4字节
        // 报文长度
        bdb.writeInt32(len);
        // 服务编码
        bdb.writeString(servCode, 6);
        // 报文frame
        bdb.writeBytes(message.getBytes());
        // 结束字节
        bdb.writeInt8((byte) BLOCK_END_SIGN);
        return bdb.getBytes();
    }

    /**
     * 解析报文
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static EntryModel deSplit(ByteDataBuffer obj) throws Exception
    {
        ByteDataBuffer dataBuf = obj;
        dataBuf.setEncoding(ENCODING);
        dataBuf.setInBigEndian(false);
        // 长度
        int totalLen = 0;
        byte sign = dataBuf.readInt8();
        if (sign != BLOCK_BGN_SIGN)
        {
            log.info("无法找到起始标记!");
            return null;
        }
        totalLen = dataBuf.readInt32();
        dataBuf.readString(6);
        byte[] dataBytes = new byte[totalLen];
        dataBuf.readBytes(dataBytes);
        String message = new String(dataBytes);
        log.info("请求的报文为:\n{}", message);

        // 报文是json格式，把json报文转换成对象类型的
        EntryModel entryModel = JSON.parseObject(message, EntryModel.class);
        EntryHeadModel headModel = JSON.parseObject(entryModel.getHead(), new TypeReference<EntryHeadModel>()
        {
        });
        EntryBodyModel bodyModel = JSON.parseObject(entryModel.getBody(), new TypeReference<EntryBodyModel>()
        {
        });
        entryModel.setHeadModel(headModel);
        entryModel.setBodyModel(bodyModel);

        return entryModel;
    }


    /**
     * 计算报文长度，不足四位左补零
     *
     * @param text    报文信息
     * @param needLen 报文长度规定的字符数
     * @return
     */
    private static String getLen(String text, int needLen)
    {
        if (text != null)
        {
            int len;
            try
            {
                len = text.getBytes("utf-8").length;
                String lenStr = String.valueOf(len);
                StringBuffer sb = new StringBuffer(lenStr);
                while (sb.length() < needLen)
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
