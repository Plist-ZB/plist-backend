package com.zerobase.plistbackend.module.refresh.exception;

import com.zerobase.plistbackend.common.app.exception.BaseException;
import com.zerobase.plistbackend.common.app.exception.ErrorStatus;

public class RefreshException extends BaseException {

  public RefreshException(
      ErrorStatus errorStatus) {
    super(errorStatus);
  }
}
