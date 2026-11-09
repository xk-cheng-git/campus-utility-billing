package com.campus.utilitybilling.mapper;

import com.campus.utilitybilling.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper {

    UserInfo selectByStudentNo(@Param("studentNo") String studentNo);

    List<UserInfo> selectAll();

    List<UserInfo> selectByDorm(@Param("dorm") String dorm);

    int insert(UserInfo userInfo);

    int update(UserInfo userInfo);

    int updateBalance(@Param("studentNo") String studentNo,
                      @Param("studentMoney") java.math.BigDecimal studentMoney);
}
