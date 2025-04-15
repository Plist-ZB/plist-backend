package com.zerobase.plistbackend.module.fcm.exception;

import com.zerobase.plistbackend.common.app.exception.BaseException;
import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class FCMTokenException extends BaseException {

  public FCMTokenException(ErrorStatus errorStatus) {
    super(errorStatus);
  }
}
