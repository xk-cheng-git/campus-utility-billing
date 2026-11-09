package com.campus.utilitybilling.mapper;

import com.campus.utilitybilling.entity.DormBill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DormBillMapper {

    DormBill selectById(@Param("billId") Long billId);

    List<DormBill> selectByCondition(@Param("dorm") String dorm,
                                     @Param("payStatus") Integer payStatus,
                                     @Param("yearMonth") String yearMonth);

    int insert(DormBill dormBill);

    int updatePayStatus(@Param("billId") Long billId,
                        @Param("payStatus") Integer payStatus);
}
