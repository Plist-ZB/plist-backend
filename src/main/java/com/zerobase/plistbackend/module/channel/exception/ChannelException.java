package com.zerobase.plistbackend.module.channel.exception;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class ChannelException extends RuntimeException{
  private final ErrorStatus errorStatus;
  private final int errorCode;
  private final String message;
  private final String errorType;

  public ChannelException(ErrorStatus errorStatus) {
    super(errorStatus.getMessage());
    this.errorStatus = errorStatus;
    this.errorCode = errorStatus.getErrorCode();
    this.message = errorStatus.getMessage();
    this.errorType = errorStatus.getErrorType();
  }
}
