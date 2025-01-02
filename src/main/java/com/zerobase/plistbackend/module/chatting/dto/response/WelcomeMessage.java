package com.zerobase.plistbackend.module.chatting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class WelcomeMessage {
  @Schema(description = "환영 메시지 내용", example = "username님이 입장하셨습니다.")
  private final String username;

  public WelcomeMessage(String username) {
    this.username = username + "님이 입장하셨습니다.";
  }
}
