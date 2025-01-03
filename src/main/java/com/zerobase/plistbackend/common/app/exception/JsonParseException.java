package com.zerobase.plistbackend.common.app.exception;

import lombok.Getter;

@Getter
public class JsonParseException extends RuntimeException {
  private final ErrorStatus errorStatus;
  private final int errorCode;
  private final String errorType;
  private final String getMessage;

  public JsonParseException(ErrorStatus errorStatus) {
    super(errorStatus.getMessage());
    this.errorStatus = errorStatus;
    this.errorCode = errorStatus.getErrorCode();
    this.errorType = errorStatus.getErrorType();
    this.getMessage = errorStatus.getMessage();
  }
}
