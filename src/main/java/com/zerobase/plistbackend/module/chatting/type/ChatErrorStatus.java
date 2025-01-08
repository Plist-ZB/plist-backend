package com.zerobase.plistbackend.module.chatting.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ChatErrorStatus implements ErrorStatus {
  NOT_HOST(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "해당 권한은 호스트만 가능합니다."
  );


  private final int errorCode;
  private final String errorType;
  private final String message;

  ChatErrorStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }
}
