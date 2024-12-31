package com.zerobase.plistbackend.module.chatting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ChatMessageRequest {
  private String username;
  private String message;
}
