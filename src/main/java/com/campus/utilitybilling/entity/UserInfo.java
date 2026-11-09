package com.campus.utilitybilling.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UserInfo {

    /** 学号（主键） */
    private String studentNo;

    /** 姓名 */
    private String username;

    /** 宿舍号 */
    private String dorm;

    /** 余额 */
    private BigDecimal studentMoney = BigDecimal.ZERO;

    /** 密码 */
    private String password;

    /** 角色 admin/student */
    private String role = "student";
}
