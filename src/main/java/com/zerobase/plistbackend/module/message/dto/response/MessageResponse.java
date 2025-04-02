package com.zerobase.plistbackend.module.message.dto.response;

import com.zerobase.plistbackend.module.message.entity.Message;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageResponse {

  private Long messageId;

  private String messageContent;

  private String messageLink;

  private boolean read;

  private String messageCreatedAt;

  public static MessageResponse of(Message message) {
    return MessageResponse.builder()
        .messageId(message.getMessageId())
        .messageContent(message.getMessageContent())
        .messageLink(message.getMessageLink())
        .read(message.isRead())
        .messageCreatedAt(convertStringFormat(message.getMessageCreatedAt()))
        .build();
  }

  private static String convertStringFormat(Timestamp messageCreatedAt) {
    LocalDateTime localDateTime = messageCreatedAt.toLocalDateTime();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
    return localDateTime.format(formatter);
  }
}
