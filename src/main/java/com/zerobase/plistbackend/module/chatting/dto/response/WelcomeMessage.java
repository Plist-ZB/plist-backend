package com.zerobase.plistbackend.module.chatting.dto.response;

import lombok.Getter;

@Getter
public class WelcomeMessage {
  private final String username;

  public WelcomeMessage(String username) {
    this.username = username + "님이 입장하셨습니다.";
  }
}
