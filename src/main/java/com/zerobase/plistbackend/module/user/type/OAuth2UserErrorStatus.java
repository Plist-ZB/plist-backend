package com.zerobase.plistbackend.module.user.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OAuth2UserErrorStatus implements ErrorStatus {
  NOT_FOUND(
      HttpStatus.NOT_FOUND.value(),
      HttpStatus.NOT_FOUND.getReasonPhrase(),
      "해당 유저는 존재하지 않는 유저입니다."
  );
  private final int errorCode;
  private final String errorType;
  private final String message;

  OAuth2UserErrorStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }
}
