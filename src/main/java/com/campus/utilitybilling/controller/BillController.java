package com.campus.utilitybilling.controller;

import com.campus.utilitybilling.dto.common.ApiResponse;
import com.campus.utilitybilling.dto.request.BillCreateRequestDto;
import com.campus.utilitybilling.dto.request.PayRequestDto;
import com.campus.utilitybilling.dto.request.RemindRequestDto;
import com.campus.utilitybilling.entity.DormBill;
import com.campus.utilitybilling.entity.PaymentRecord;
import com.campus.utilitybilling.service.BillService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public ApiResponse<List<DormBill>> queryBills(
            @RequestParam(required = false) String dorm,
            @RequestParam(required = false) Integer payStatus,
            @RequestParam(required = false) String yearMonth) {
        List<DormBill> bills = billService.queryBills(dorm, payStatus, yearMonth);
        return ApiResponse.success(bills);
    }

    @GetMapping("/{billId}")
    public ApiResponse<DormBill> getBill(@PathVariable Long billId) {
        try {
            DormBill bill = billService.getBillById(billId);
            return ApiResponse.success(bill);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<DormBill> createBill(@RequestBody BillCreateRequestDto request) {
        try {
            DormBill bill = billService.createBill(request);
            return ApiResponse.success("账单创建成功", bill);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PutMapping("/{billId}/pay")
    public ApiResponse<PaymentRecord> payBill(@PathVariable Long billId,
                                              @RequestBody PayRequestDto request) {
        try {
            PaymentRecord record = billService.payBill(billId, request);
            return ApiResponse.success("缴费成功", record);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/remind")
    public ApiResponse<Map<String, Object>> remind(@RequestBody RemindRequestDto request) {
        try {
            int count = billService.remind(request);
            Map<String, Object> result = new HashMap<>();
            result.put("sentCount", count);
            return ApiResponse.success("催缴完成，共发送 " + count + " 条消息", result);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
