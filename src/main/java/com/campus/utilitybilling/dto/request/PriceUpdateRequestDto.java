package com.campus.utilitybilling.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceUpdateRequestDto {

    /** 操作人学号（需为管理员） */
    private String operatorNo;

    /** 水单价 */
    private BigDecimal waterPrice;

    /** 电单价 */
    private BigDecimal electricPrice;
}
