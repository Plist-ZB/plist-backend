package com.zerobase.plistbackend.module.websocket.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageRequest {
  private String sender;
  private String message;
}
