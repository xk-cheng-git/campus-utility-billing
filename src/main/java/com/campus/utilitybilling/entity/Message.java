package com.campus.utilitybilling.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Message {

    /** 消息ID */
    private Long msgId;

    /** 学生学号 */
    private String studentNo;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** 消息类型（notice=通知, warning=警告） */
    private String msgType = "notice";

    /** 创建时间 */
    private Date createTime = new Date();

    /** 发送人（admin=管理员, system=系统自动） */
    private String sender;

    /** 是否已读 0=未读 1=已读 */
    private Integer isRead = 0;
}
