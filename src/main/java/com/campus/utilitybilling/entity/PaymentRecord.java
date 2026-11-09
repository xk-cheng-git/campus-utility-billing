package com.campus.utilitybilling.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class PaymentRecord {

    /** 缴费记录ID */
    private Long payId;

    /** 账单ID */
    private Long billId;

    /** 学生学号 */
    private String studentNo;

    /** 支付类型 1=缴账单 2=余额充值 */
    private Integer payType;

    /** 支付金额 */
    private BigDecimal payMoney;

    /** 支付时间 */
    private Date payTime = new Date();
}
