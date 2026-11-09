package com.campus.utilitybilling.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {

    /** 学号 */
    private String studentNo;

    /** 密码 */
    private String password;
}
