package com.campus.utilitybilling.service.impl;

import com.campus.utilitybilling.dto.request.ChangePasswordRequestDto;
import com.campus.utilitybilling.dto.request.LoginRequestDto;
import com.campus.utilitybilling.dto.request.TopUpRequestDto;
import com.campus.utilitybilling.dto.response.LoginResponse;
import com.campus.utilitybilling.entity.Message;
import com.campus.utilitybilling.entity.PaymentRecord;
import com.campus.utilitybilling.entity.UserInfo;
import com.campus.utilitybilling.mapper.MessageMapper;
import com.campus.utilitybilling.mapper.PaymentRecordMapper;
import com.campus.utilitybilling.mapper.UserInfoMapper;
import com.campus.utilitybilling.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private final UserInfoMapper userInfoMapper;
    private final PaymentRecordMapper paymentRecordMapper;
    private final MessageMapper messageMapper;

    public UserServiceImpl(UserInfoMapper userInfoMapper,
                           PaymentRecordMapper paymentRecordMapper,
                           MessageMapper messageMapper) {
        this.userInfoMapper = userInfoMapper;
        this.paymentRecordMapper = paymentRecordMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    public LoginResponse login(LoginRequestDto request) {
        UserInfo user = userInfoMapper.selectByStudentNo(request.getStudentNo());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 弱密码检测：密码等于学号时，发送安全提醒
        if (request.getPassword().equals(request.getStudentNo())) {
            try {
                Message warning = new Message();
                warning.setStudentNo(user.getStudentNo());
                warning.setTitle("安全提醒");
                warning.setContent("您的密码与学号相同，建议尽快修改密码以确保账户安全。");
                warning.setMsgType("warning");
                warning.setSender("system");
                warning.setCreateTime(new Date());
                warning.setIsRead(0);
                messageMapper.insert(warning);
            } catch (Exception ignored) {
                // 提醒消息发送失败不影响登录
            }
        }

        LoginResponse response = new LoginResponse();
        response.setStudentNo(user.getStudentNo());
        response.setUsername(user.getUsername());
        response.setDorm(user.getDorm());
        response.setRole(user.getRole());
        response.setAdmin("admin".equals(user.getRole()));
        response.setStudentMoney(user.getStudentMoney());
        return response;
    }

    @Override
    public UserInfo getUserByStudentNo(String studentNo) {
        UserInfo user = userInfoMapper.selectByStudentNo(studentNo);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(null);
        return user;
    }

    @Override
    @Transactional
    public PaymentRecord topUp(String studentNo, TopUpRequestDto request) {
        UserInfo user = userInfoMapper.selectByStudentNo(studentNo);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("充值金额必须大于0");
        }

        // 更新余额
        BigDecimal newBalance = user.getStudentMoney().add(request.getAmount())
                .setScale(2, RoundingMode.DOWN);
        userInfoMapper.updateBalance(studentNo, newBalance);

        // 生成充值记录
        PaymentRecord record = new PaymentRecord();
        record.setBillId(null);
        record.setStudentNo(studentNo);
        record.setPayType(2);  // 2=余额充值
        record.setPayMoney(request.getAmount());
        record.setPayTime(new Date());
        paymentRecordMapper.insert(record);

        return record;
    }

    @Override
    public void changePassword(String studentNo, ChangePasswordRequestDto request) {
        UserInfo user = userInfoMapper.selectByStudentNo(studentNo);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new RuntimeException("旧密码错误");
        }
        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            throw new RuntimeException("新密码不能为空");
        }
        user.setPassword(request.getNewPassword());
        userInfoMapper.update(user);
    }

    @Override
    public UserInfo verifyAdmin(String studentNo) {
        UserInfo user = userInfoMapper.selectByStudentNo(studentNo);
        if (user == null) {
            throw new RuntimeException("用户不存在，学号：" + studentNo);
        }
        if (!"admin".equals(user.getRole())) {
            throw new RuntimeException("无管理员权限，当前角色：" + user.getRole());
        }
        return user;
    }
}
