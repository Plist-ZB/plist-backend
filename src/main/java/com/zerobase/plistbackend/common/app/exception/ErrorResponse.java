package com.zerobase.plistbackend.common.app.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
  private final int errorCode;
  private final String errorType;
  private final String message;

  private ErrorResponse(ErrorStatus errorStatus) {
    this.errorCode = errorStatus.getErrorCode();
    this.errorType = errorStatus.getErrorType();
    this.message = errorStatus.getMessage();
  }

  public static ErrorResponse create(ErrorStatus errorStatus) {
    return new ErrorResponse(errorStatus);
  }
}
