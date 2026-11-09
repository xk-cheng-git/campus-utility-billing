package com.campus.utilitybilling.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DormBill {

    /** 账单ID 主键 */
    private Long billId;

    /** 宿舍号 */
    private String dorm;

    /** 年月 如 2025-05 */
    private String yearMonth;

    /** 用水量 */
    private BigDecimal waterUsage = BigDecimal.ZERO;

    /** 用电量 */
    private BigDecimal electricUsage = BigDecimal.ZERO;

    /** 总金额 */
    private BigDecimal totalMoney = BigDecimal.ZERO;

    /** 支付状态 0未支付 1已支付 */
    private Integer payStatus = 0;

    /** 创建人（管理员学号） */
    private String createBy;
}
