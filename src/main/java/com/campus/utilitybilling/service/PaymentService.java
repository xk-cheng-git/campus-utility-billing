package com.campus.utilitybilling.service;

import com.campus.utilitybilling.entity.PaymentRecord;

import java.util.List;

public interface PaymentService {

    /**
     * 根据学号查询缴费记录
     */
    List<PaymentRecord> queryByStudentNo(String studentNo);

    /**
     * 根据ID查询缴费记录详情
     */
    PaymentRecord getById(Long payId);
}
