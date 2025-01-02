package com.zerobase.plistbackend.common.app.exception;

public interface ErrorStatus {

  int getErrorCode();

  String getMessage();

  String getErrorType();

}
