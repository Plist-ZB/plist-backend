package com.zerobase.plistbackend.module.chatting.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
public class ChatMessageRequest {
  private String sender;
  private String message;
  private String thumbnail;
}
