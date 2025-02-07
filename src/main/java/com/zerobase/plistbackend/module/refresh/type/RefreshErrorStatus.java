package com.zerobase.plistbackend.module.refresh.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RefreshErrorStatus implements ErrorStatus {

  REFRESH_NOT_FOUND(
      HttpStatus.NOT_FOUND.value(),
      HttpStatus.NOT_FOUND.getReasonPhrase(),
      "해당 리프레시 토큰은 존재하지 않습니다."
  ),

  REFRESH_EXPIRED(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "해당 리프레시 토큰은 만료되었습니다."
  ),

  REFRESH_INVALID(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "해당 토큰은 리프레시 토큰이 아닙니다."
  );

  private final int errorCode;
  private final String errorType;
  private final String message;

  RefreshErrorStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }
}
