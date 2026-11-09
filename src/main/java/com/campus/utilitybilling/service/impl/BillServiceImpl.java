package com.campus.utilitybilling.service.impl;

import com.campus.utilitybilling.dto.request.BillCreateRequestDto;
import com.campus.utilitybilling.dto.request.PayRequestDto;
import com.campus.utilitybilling.dto.request.RemindRequestDto;
import com.campus.utilitybilling.entity.DormBill;
import com.campus.utilitybilling.entity.Message;
import com.campus.utilitybilling.entity.PaymentRecord;
import com.campus.utilitybilling.entity.SysConfig;
import com.campus.utilitybilling.entity.UserInfo;
import com.campus.utilitybilling.mapper.DormBillMapper;
import com.campus.utilitybilling.mapper.MessageMapper;
import com.campus.utilitybilling.mapper.PaymentRecordMapper;
import com.campus.utilitybilling.mapper.SysConfigMapper;
import com.campus.utilitybilling.mapper.UserInfoMapper;
import com.campus.utilitybilling.service.BillService;
import com.campus.utilitybilling.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BillServiceImpl implements BillService {

    private final DormBillMapper dormBillMapper;
    private final SysConfigMapper sysConfigMapper;
    private final UserInfoMapper userInfoMapper;
    private final PaymentRecordMapper paymentRecordMapper;
    private final MessageMapper messageMapper;
    private final UserService userService;

    public BillServiceImpl(DormBillMapper dormBillMapper,
                           SysConfigMapper sysConfigMapper,
                           UserInfoMapper userInfoMapper,
                           PaymentRecordMapper paymentRecordMapper,
                           MessageMapper messageMapper,
                           UserService userService) {
        this.dormBillMapper = dormBillMapper;
        this.sysConfigMapper = sysConfigMapper;
        this.userInfoMapper = userInfoMapper;
        this.paymentRecordMapper = paymentRecordMapper;
        this.messageMapper = messageMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public DormBill createBill(BillCreateRequestDto request) {
        // 校验操作人是否为管理员
        userService.verifyAdmin(request.getCreateBy());

        // 获取当前水电单价
        SysConfig config = sysConfigMapper.selectCurrent();
        if (config == null) {
            throw new RuntimeException("系统配置不存在，请先设置水电单价");
        }

        // 计算总金额（保留2位小数，不四舍五入进位，直接截断）
        BigDecimal waterCost = request.getWaterUsage().multiply(config.getWaterPrice())
                .setScale(2, RoundingMode.DOWN);
        BigDecimal electricCost = request.getElectricUsage().multiply(config.getElectricPrice())
                .setScale(2, RoundingMode.DOWN);
        BigDecimal totalMoney = waterCost.add(electricCost);

        DormBill bill = new DormBill();
        bill.setDorm(request.getDorm());
        bill.setYearMonth(request.getYearMonth());
        bill.setWaterUsage(request.getWaterUsage());
        bill.setElectricUsage(request.getElectricUsage());
        bill.setTotalMoney(totalMoney);
        bill.setPayStatus(0);
        bill.setCreateBy(request.getCreateBy());

        dormBillMapper.insert(bill);

        // 自动通知：向同宿舍所有学生发送缴费提醒
        List<UserInfo> dormStudents = userInfoMapper.selectByDorm(request.getDorm());
        if (dormStudents != null && !dormStudents.isEmpty()) {
            for (UserInfo student : dormStudents) {
                Message notice = new Message();
                notice.setStudentNo(student.getStudentNo());
                notice.setTitle("缴费提醒");
                notice.setContent("您宿舍 " + request.getDorm() + " 的 " + request.getYearMonth()
                        + " 水电费账单已生成，金额 " + totalMoney + " 元，请及时缴费。");
                notice.setMsgType("notice");
                notice.setSender(request.getCreateBy());
                notice.setCreateTime(new Date());
                notice.setIsRead(0);
                messageMapper.insert(notice);
            }
        }

        return bill;
    }

    @Override
    @Transactional
    public PaymentRecord payBill(Long billId, PayRequestDto request) {
        DormBill bill = dormBillMapper.selectById(billId);
        if (bill == null) {
            throw new RuntimeException("账单不存在");
        }
        if (bill.getPayStatus() == 1) {
            throw new RuntimeException("该账单已支付");
        }

        UserInfo user = userInfoMapper.selectByStudentNo(request.getStudentNo());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (user.getStudentMoney().compareTo(bill.getTotalMoney()) < 0) {
            throw new RuntimeException("余额不足，当前余额：" + user.getStudentMoney()
                    + "，需支付：" + bill.getTotalMoney());
        }

        BigDecimal newBalance = user.getStudentMoney().subtract(bill.getTotalMoney())
                .setScale(2, RoundingMode.DOWN);
        userInfoMapper.updateBalance(user.getStudentNo(), newBalance);

        dormBillMapper.updatePayStatus(billId, 1);

        PaymentRecord record = new PaymentRecord();
        record.setBillId(billId);
        record.setStudentNo(request.getStudentNo());
        record.setPayType(1);
        record.setPayMoney(bill.getTotalMoney());
        record.setPayTime(new Date());
        paymentRecordMapper.insert(record);

        return record;
    }

    @Override
    public List<DormBill> queryBills(String dorm, Integer payStatus, String yearMonth) {
        return dormBillMapper.selectByCondition(dorm, payStatus, yearMonth);
    }

    @Override
    public DormBill getBillById(Long billId) {
        DormBill bill = dormBillMapper.selectById(billId);
        if (bill == null) {
            throw new RuntimeException("账单不存在");
        }
        return bill;
    }

    @Override
    @Transactional
    public int remind(RemindRequestDto request) {
        // 校验操作人是否为管理员
        userService.verifyAdmin(request.getOperatorNo());

        // 查询未缴费账单（按宿舍过滤可选）
        List<DormBill> unpaidBills = dormBillMapper.selectByCondition(
                request.getDorm(), 0, null);

        if (unpaidBills == null || unpaidBills.isEmpty()) {
            return 0;
        }

        // 收集所有未缴费的宿舍号（去重）
        Set<String> unpaidDorms = new HashSet<>();
        for (DormBill bill : unpaidBills) {
            unpaidDorms.add(bill.getDorm());
        }

        // 收集所有需通知的学生（去重）
        Set<String> notifiedStudents = new HashSet<>();
        int totalSent = 0;

        for (String dorm : unpaidDorms) {
            List<UserInfo> students = userInfoMapper.selectByDorm(dorm);
            if (students == null) continue;

            for (UserInfo student : students) {
                // 同一学生只发一次
                if (notifiedStudents.add(student.getStudentNo())) {
                    Message reminder = new Message();
                    reminder.setStudentNo(student.getStudentNo());
                    reminder.setTitle("催缴通知");
                    reminder.setContent("您宿舍 " + dorm
                            + " 尚有水电费未缴纳，请尽快缴费，以免影响正常用电用水。");
                    reminder.setMsgType("warning");
                    reminder.setSender(request.getOperatorNo());
                    reminder.setCreateTime(new Date());
                    reminder.setIsRead(0);
                    messageMapper.insert(reminder);
                    totalSent++;
                }
            }
        }

        return totalSent;
    }
}
