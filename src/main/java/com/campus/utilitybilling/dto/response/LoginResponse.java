package com.campus.utilitybilling.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoginResponse {

    /** 学号 */
    private String studentNo;

    /** 姓名 */
    private String username;

    /** 宿舍号 */
    private String dorm;

    /** 角色 admin / student */
    private String role;

    /** 是否为管理员（JSON字段名自动为 admin，因Lombok对boolean去掉is前缀） */
    private boolean isAdmin;

    /** 账户余额 */
    private BigDecimal studentMoney;
}
