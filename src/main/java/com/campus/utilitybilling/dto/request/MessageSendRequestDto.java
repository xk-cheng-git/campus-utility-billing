package com.campus.utilitybilling.dto.request;

import lombok.Data;

@Data
public class MessageSendRequestDto {

    /** 接收学生学号 */
    private String studentNo;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** 消息类型 */
    private String msgType;

    /** 发送人 */
    private String sender;
}
