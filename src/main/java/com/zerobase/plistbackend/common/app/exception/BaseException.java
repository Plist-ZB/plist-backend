package com.zerobase.plistbackend.common.app.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

  private final ErrorStatus errorStatus;
  private final int errorCode;
  private final String errorType;
  private final String message;

  public BaseException(final ErrorStatus errorStatus) {
    this.errorStatus = errorStatus;
    this.errorCode = errorStatus.getErrorCode();
    this.errorType = errorStatus.getErrorType();
    this.message = errorStatus.getMessage();
  }

}
