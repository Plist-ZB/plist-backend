package com.zerobase.plistbackend.module.fcm.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FCMTokenErrorStatus implements ErrorStatus {
  NOT_FOUND(
      HttpStatus.NOT_FOUND.value(),
      HttpStatus.NOT_FOUND.getReasonPhrase(),
      "해당하는 토큰은 존재하지 않습니다"
  ),
  NOT_MY_TOKEN(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "본인의 토큰만 삭제가 가능합니다."
  );

  private final int errorCode;
  private final String errorType;
  private final String message;

  FCMTokenErrorStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }
}
