package com.zerobase.plistbackend.module.user.exception;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class OAuth2UserExceptionm extends RuntimeException{
  private final ErrorStatus errorStatus;
  private final int errorCode;
  private final String errorType;
  private final String message;

  public OAuth2UserExceptionm(ErrorStatus errorStatus) {
    super(errorStatus.getMessage());
    this.errorStatus = errorStatus;
    this.errorCode = errorStatus.getErrorCode();
    this.errorType = errorStatus.getErrorType();
    this.message = errorStatus.getMessage();
  }
}
