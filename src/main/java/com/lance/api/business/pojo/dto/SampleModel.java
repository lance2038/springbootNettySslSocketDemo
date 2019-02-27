package com.lance.api.business.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author lance
 * @since 2018-09-25
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SampleModel
{
    /**
     * 总数
     */
    public int count;
    /**
     * 总额
     */
    public BigDecimal amt;

}
