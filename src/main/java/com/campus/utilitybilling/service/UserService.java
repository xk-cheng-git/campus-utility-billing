package com.campus.utilitybilling.service;

import com.campus.utilitybilling.dto.request.ChangePasswordRequestDto;
import com.campus.utilitybilling.dto.request.LoginRequestDto;
import com.campus.utilitybilling.dto.request.TopUpRequestDto;
import com.campus.utilitybilling.dto.response.LoginResponse;
import com.campus.utilitybilling.entity.PaymentRecord;
import com.campus.utilitybilling.entity.UserInfo;

public interface UserService {

    /**
     * 用户登录认证
     * @return 登录响应（含 isAdmin 标记，不含密码）
     */
    LoginResponse login(LoginRequestDto request);

    /**
     * 根据学号查询用户信息
     */
    UserInfo getUserByStudentNo(String studentNo);

    /**
     * 账户充值
     * @return 充值支付记录
     */
    PaymentRecord topUp(String studentNo, TopUpRequestDto request);

    /**
     * 校验是否为管理员，不是则抛异常
     * @return 操作人的 UserInfo
     */
    UserInfo verifyAdmin(String studentNo);

    /**
     * 修改密码
     */
    void changePassword(String studentNo, ChangePasswordRequestDto request);
}
