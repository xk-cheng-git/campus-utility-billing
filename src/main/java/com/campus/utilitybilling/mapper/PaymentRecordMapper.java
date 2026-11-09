package com.campus.utilitybilling.mapper;

import com.campus.utilitybilling.entity.PaymentRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentRecordMapper {

    PaymentRecord selectById(@Param("payId") Long payId);

    List<PaymentRecord> selectByStudentNo(@Param("studentNo") String studentNo);

    int insert(PaymentRecord paymentRecord);
}
