package com.campus.utilitybilling.service;

import com.campus.utilitybilling.dto.request.BillCreateRequestDto;
import com.campus.utilitybilling.dto.request.PayRequestDto;
import com.campus.utilitybilling.dto.request.RemindRequestDto;
import com.campus.utilitybilling.entity.DormBill;
import com.campus.utilitybilling.entity.PaymentRecord;

import java.util.List;

public interface BillService {

    /**
     * 创建账单（仅管理员可操作；自动通知同宿舍学生）
     */
    DormBill createBill(BillCreateRequestDto request);

    /**
     * 支付账单
     */
    PaymentRecord payBill(Long billId, PayRequestDto request);

    /**
     * 多条件查询账单
     */
    List<DormBill> queryBills(String dorm, Integer payStatus, String yearMonth);

    /**
     * 根据ID查询账单详情
     */
    DormBill getBillById(Long billId);

    /**
     * 催缴：向未缴费宿舍学生发送催缴消息，返回发送条数
     */
    int remind(RemindRequestDto request);
}
