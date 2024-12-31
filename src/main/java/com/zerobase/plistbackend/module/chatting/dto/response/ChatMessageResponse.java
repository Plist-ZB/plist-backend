package com.zerobase.plistbackend.module.chatting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ChatMessageResponse {
  private String username;
  private String message;
}
