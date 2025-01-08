package com.zerobase.plistbackend.module.chatting.dto.request;

import lombok.Getter;

@Getter
public class ChatMessageRequest {
  private String sender;
  private String message;
}
