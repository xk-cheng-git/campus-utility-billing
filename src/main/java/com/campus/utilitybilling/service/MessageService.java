package com.campus.utilitybilling.service;

import com.campus.utilitybilling.dto.request.MessageSendRequestDto;
import com.campus.utilitybilling.entity.Message;

import java.util.List;

public interface MessageService {

    /**
     * 查询学生的消息
     */
    List<Message> queryByStudentNo(String studentNo);

    /**
     * 发送消息（仅管理员可操作，根据 sender 校验权限）
     */
    Message sendMessage(MessageSendRequestDto request);

    /**
     * 标记消息为已读
     */
    void markAsRead(Long msgId);

    /**
     * 查询未读消息数
     */
    int countUnread(String studentNo);
}
