package com.campus.utilitybilling.controller;

import com.campus.utilitybilling.dto.common.ApiResponse;
import com.campus.utilitybilling.dto.request.ChangePasswordRequestDto;
import com.campus.utilitybilling.dto.request.TopUpRequestDto;
import com.campus.utilitybilling.entity.PaymentRecord;
import com.campus.utilitybilling.entity.UserInfo;
import com.campus.utilitybilling.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{studentNo}")
    public ApiResponse<UserInfo> getUser(@PathVariable String studentNo) {
        try {
            UserInfo user = userService.getUserByStudentNo(studentNo);
            return ApiResponse.success(user);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PutMapping("/{studentNo}/top-up")
    public ApiResponse<PaymentRecord> topUp(@PathVariable String studentNo,
                                            @RequestBody TopUpRequestDto request) {
        try {
            PaymentRecord record = userService.topUp(studentNo, request);
            return ApiResponse.success("充值成功", record);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PutMapping("/{studentNo}/change-password")
    public ApiResponse<Void> changePassword(@PathVariable String studentNo,
                                            @RequestBody ChangePasswordRequestDto request) {
        try {
            userService.changePassword(studentNo, request);
            return ApiResponse.success("密码修改成功", null);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
