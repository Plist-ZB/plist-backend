package com.zerobase.plistbackend.module.chatting.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatMessageRequest {
  private String sender;
  private String message;
  private String thumbnail;
}
