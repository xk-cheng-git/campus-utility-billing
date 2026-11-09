package com.campus.utilitybilling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面路由控制器 — 负责返回 Thymeleaf 模板页面
 * 所有数据交互通过前端 AJAX 调用 REST API 完成
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ==================== 学生端 ====================

    @GetMapping("/student")
    public String studentIndex() {
        return "student/index";
    }

    @GetMapping("/student/bills")
    public String studentBills() {
        return "student/bills";
    }

    @GetMapping("/student/payments")
    public String studentPayments() {
        return "student/payments";
    }

    @GetMapping("/student/messages")
    public String studentMessages() {
        return "student/messages";
    }

    @GetMapping("/student/profile")
    public String studentProfile() {
        return "student/profile";
    }

    // ==================== 管理端 ====================

    @GetMapping("/admin")
    public String adminIndex() {
        return "admin/index";
    }

    @GetMapping("/admin/bills")
    public String adminBills() {
        return "admin/bills";
    }

    @GetMapping("/admin/config")
    public String adminConfig() {
        return "admin/config";
    }

    @GetMapping("/admin/messages")
    public String adminMessages() {
        return "admin/messages";
    }

    @GetMapping("/admin/users")
    public String adminUsers() {
        return "admin/users";
    }
}
