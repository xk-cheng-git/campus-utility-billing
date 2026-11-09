package com.campus.utilitybilling.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillCreateRequestDto {

    /** 宿舍号 */
    private String dorm;

    /** 年月 如 2025-05 */
    private String yearMonth;

    /** 用水量 */
    private BigDecimal waterUsage;

    /** 用电量 */
    private BigDecimal electricUsage;

    /** 创建人（管理员学号） */
    private String createBy;
}
