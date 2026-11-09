package com.campus.utilitybilling.service.impl;

import com.campus.utilitybilling.dto.request.MessageSendRequestDto;
import com.campus.utilitybilling.entity.Message;
import com.campus.utilitybilling.mapper.MessageMapper;
import com.campus.utilitybilling.service.MessageService;
import com.campus.utilitybilling.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;
    private final UserService userService;

    public MessageServiceImpl(MessageMapper messageMapper,
                              UserService userService) {
        this.messageMapper = messageMapper;
        this.userService = userService;
    }

    @Override
    public List<Message> queryByStudentNo(String studentNo) {
        return messageMapper.selectByStudentNo(studentNo);
    }

    @Override
    @Transactional
    public Message sendMessage(MessageSendRequestDto request) {
        // 校验发送人是否为管理员
        userService.verifyAdmin(request.getSender());

        Message message = new Message();
        message.setStudentNo(request.getStudentNo());
        message.setTitle(request.getTitle());
        message.setContent(request.getContent());
        message.setMsgType(request.getMsgType() != null ? request.getMsgType() : "notice");
        message.setSender(request.getSender());
        message.setCreateTime(new Date());
        message.setIsRead(0);

        messageMapper.insert(message);
        return message;
    }

    @Override
    public void markAsRead(Long msgId) {
        messageMapper.markAsRead(msgId);
    }

    @Override
    public int countUnread(String studentNo) {
        return messageMapper.countUnread(studentNo);
    }
}
