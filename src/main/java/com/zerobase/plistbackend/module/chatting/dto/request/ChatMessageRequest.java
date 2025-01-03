package com.zerobase.plistbackend.module.chatting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
  private String username;
  private String message;
}
