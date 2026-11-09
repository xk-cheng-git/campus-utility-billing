package com.campus.utilitybilling.controller;

import com.campus.utilitybilling.dto.common.ApiResponse;
import com.campus.utilitybilling.entity.PaymentRecord;
import com.campus.utilitybilling.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ApiResponse<List<PaymentRecord>> queryPayments(
            @RequestParam(required = false) String studentNo) {
        if (studentNo == null || studentNo.isEmpty()) {
            return ApiResponse.error(400, "请提供学号");
        }
        List<PaymentRecord> records = paymentService.queryByStudentNo(studentNo);
        return ApiResponse.success(records);
    }

    @GetMapping("/{payId}")
    public ApiResponse<PaymentRecord> getPayment(@PathVariable Long payId) {
        try {
            PaymentRecord record = paymentService.getById(payId);
            return ApiResponse.success(record);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
