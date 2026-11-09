package com.campus.utilitybilling.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SysConfig {

    /** 配置ID */
    private Long priceId;

    /** 水单价 */
    private BigDecimal waterPrice;

    /** 电单价 */
    private BigDecimal electricPrice;
}
