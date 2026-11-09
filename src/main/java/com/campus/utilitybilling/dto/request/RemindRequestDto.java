package com.campus.utilitybilling.dto.request;

import lombok.Data;

@Data
public class RemindRequestDto {

    /** 操作人学号（管理员） */
    private String operatorNo;

    /** 宿舍号（可选，不传则催缴所有未缴费宿舍） */
    private String dorm;
}
