package com.lance.api.business.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author lance
 * @version v0.0.1
 * @describe 接收报文--包头:是报文数据头信息，记录了一系列约定数据
 * @since 2018/10/11
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryHeadModel
{
    /**
     * 报文id
     */
    private String msgId;
    /**
     * 报文的发送时间，时间格式为 yyyyMMddHH24miss
     */
    private String msgTime;
    /**
     * 服务编号。详见各个接口
     */
    private String servCode;
    /**
     * 报文的版本号
     */
    private String version;
    /**
     * 预留字段
     */
    private String extend;
}
