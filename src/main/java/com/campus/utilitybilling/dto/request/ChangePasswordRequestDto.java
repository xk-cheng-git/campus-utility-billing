package com.campus.utilitybilling.dto.request;

import lombok.Data;

@Data
public class ChangePasswordRequestDto {

    /** 旧密码 */
    private String oldPassword;

    /** 新密码 */
    private String newPassword;
}
