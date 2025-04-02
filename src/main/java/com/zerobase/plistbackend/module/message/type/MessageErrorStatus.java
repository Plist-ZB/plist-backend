package com.zerobase.plistbackend.module.message.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MessageErrorStatus implements ErrorStatus {
  NOT_FOUND(
      HttpStatus.NOT_FOUND.value(),
      HttpStatus.NOT_FOUND.getReasonPhrase(),
      "해당 메시지는 존재하지 않습니다"
  ),
  NOT_MY_MESSAGE(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "본인의 메시지만 읽음처리 가능합니다."
  );

  private final int errorCode;
  private final String errorType;
  private final String message;

  MessageErrorStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }
}
