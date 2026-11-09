package com.campus.utilitybilling.controller;

import com.campus.utilitybilling.dto.common.ApiResponse;
import com.campus.utilitybilling.dto.request.LoginRequestDto;
import com.campus.utilitybilling.dto.response.LoginResponse;
import com.campus.utilitybilling.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequestDto request) {
        try {
            LoginResponse response = userService.login(request);
            return ApiResponse.success("登录成功", response);
        } catch (RuntimeException e) {
            return ApiResponse.error(401, e.getMessage());
        }
    }
}
