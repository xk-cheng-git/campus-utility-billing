package com.campus.utilitybilling.controller;

import com.campus.utilitybilling.dto.common.ApiResponse;
import com.campus.utilitybilling.dto.request.MessageSendRequestDto;
import com.campus.utilitybilling.entity.Message;
import com.campus.utilitybilling.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ApiResponse<List<Message>> queryMessages(
            @RequestParam(required = false) String studentNo) {
        if (studentNo == null || studentNo.isEmpty()) {
            return ApiResponse.error(400, "请提供学号");
        }
        List<Message> messages = messageService.queryByStudentNo(studentNo);
        return ApiResponse.success(messages);
    }

    @PostMapping
    public ApiResponse<Message> sendMessage(@RequestBody MessageSendRequestDto request) {
        try {
            Message message = messageService.sendMessage(request);
            return ApiResponse.success("消息发送成功", message);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PutMapping("/{msgId}/read")
    public ApiResponse<Void> markAsRead(@PathVariable Long msgId) {
        messageService.markAsRead(msgId);
        return ApiResponse.success("已标记为已读", null);
    }

    @GetMapping("/unread-count")
    public ApiResponse<Map<String, Integer>> countUnread(
            @RequestParam String studentNo) {
        int count = messageService.countUnread(studentNo);
        Map<String, Integer> result = new HashMap<>();
        result.put("unreadCount", count);
        return ApiResponse.success(result);
    }
}
