package com.lance.api.business.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author lance
 * @version v0.0.1
 * @describe 接收报文
 * @since 2018/10/11
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryModel
{
    private String head;
    private String body;
    private EntryHeadModel headModel;
    private EntryBodyModel bodyModel;
}
