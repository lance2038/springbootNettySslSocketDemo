package com.lance.api.business.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author lance
 * @version v0.0.1
 * @describe 接收报文-包体:是报文数据主体内容，承载了报文的业务数据，具体每个报文包含了哪些业务数据将在具体的业务接口中详细描述
 * @since 2018/10/11
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryBodyModel
{
    /**
     * 查询条件的类别
     */
    private String queryType;
    /**
     * 对应查询类别的查询条件。
     */
    private String queryValue;
    /**
     * 唯一标识
     */
    private String key;
    /**
     * 预留
     */
    private String extend;
}
