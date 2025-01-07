package com.zerobase.plistbackend.module.channel.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ChannelErrorStatus implements ErrorStatus {
  NOT_FOUND(
      HttpStatus.NOT_FOUND.value(),
      HttpStatus.NOT_FOUND.getReasonPhrase(),
      "해당 채널은 존재하지 않습니다"
  ),;

  private final int errorCode;
  private final String message;
  private final String errorType;

  ChannelErrorStatus(int errorCode, String message, String errorType) {
    this.errorCode = errorCode;
    this.message = message;
    this.errorType = errorType;
  }
}
