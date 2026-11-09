package com.campus.utilitybilling.service.impl;

import com.campus.utilitybilling.entity.PaymentRecord;
import com.campus.utilitybilling.mapper.PaymentRecordMapper;
import com.campus.utilitybilling.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRecordMapper paymentRecordMapper;

    public PaymentServiceImpl(PaymentRecordMapper paymentRecordMapper) {
        this.paymentRecordMapper = paymentRecordMapper;
    }

    @Override
    public List<PaymentRecord> queryByStudentNo(String studentNo) {
        return paymentRecordMapper.selectByStudentNo(studentNo);
    }

    @Override
    public PaymentRecord getById(Long payId) {
        PaymentRecord record = paymentRecordMapper.selectById(payId);
        if (record == null) {
            throw new RuntimeException("缴费记录不存在");
        }
        return record;
    }
}
