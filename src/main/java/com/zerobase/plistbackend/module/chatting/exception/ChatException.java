package com.zerobase.plistbackend.module.chatting.exception;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class ChatException extends RuntimeException{
  private final ErrorStatus errorStatus;
  private final int errorCode;
  private final String message;
  private final String errorType;

  public ChatException(ErrorStatus errorStatus) {
    super(errorStatus.getMessage());
    this.errorStatus = errorStatus;
    this.errorCode = errorStatus.getErrorCode();
    this.message = errorStatus.getMessage();
    this.errorType = errorStatus.getErrorType();
  }
}
