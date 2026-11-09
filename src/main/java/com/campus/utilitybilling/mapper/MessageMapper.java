package com.campus.utilitybilling.mapper;

import com.campus.utilitybilling.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {

    List<Message> selectByStudentNo(@Param("studentNo") String studentNo);

    List<Message> selectByStudentNoAndType(@Param("studentNo") String studentNo,
                                           @Param("msgType") String msgType);

    int insert(Message message);

    int markAsRead(@Param("msgId") Long msgId);

    int countUnread(@Param("studentNo") String studentNo);
}
