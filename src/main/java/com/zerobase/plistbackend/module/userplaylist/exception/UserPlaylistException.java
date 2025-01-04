package com.zerobase.plistbackend.module.userplaylist.exception;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class UserPlaylistException extends RuntimeException{
  private final ErrorStatus errorStatus;
  private final int getErrorCode;
  private final String getMessage;
  private final String getErrorType;

  public UserPlaylistException(ErrorStatus errorStatus) {
    super(errorStatus.getMessage());
    this.errorStatus = errorStatus;
    this.getErrorCode = errorStatus.getErrorCode();
    this.getErrorType = errorStatus.getErrorType();
    this.getMessage = errorStatus.getMessage();
  }

}
