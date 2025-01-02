package com.zerobase.plistbackend.module.chatting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
  private String username;
  private String message;
}
