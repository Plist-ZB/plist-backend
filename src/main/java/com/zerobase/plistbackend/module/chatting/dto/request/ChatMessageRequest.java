package com.zerobase.plistbackend.module.chatting.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageRequest {
  private String sender;
  private String message;
}
