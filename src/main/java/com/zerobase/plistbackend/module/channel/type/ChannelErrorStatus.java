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
  ),
  ALREADY_ENTER(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "이미 채널에 참가 중 입니다."
  ),
  NOT_ENTER(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "현재 채널에 참가 중이지 않습니다."
  ),
  NOT_HOST(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "해당 권한은 호스트만 가능합니다."
  ),
  NOT_STREAMING(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "해당 채널은 종료되었습니다."
  );

  private final int errorCode;
  private final String errorType;
  private final String message;

  ChannelErrorStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }
}
