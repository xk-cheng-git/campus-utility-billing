package com.campus.utilitybilling.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopUpRequestDto {

    /** 充值金额 */
    private BigDecimal amount;
}
