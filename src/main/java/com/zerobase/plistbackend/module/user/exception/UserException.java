package com.zerobase.plistbackend.module.user.exception;

import com.zerobase.plistbackend.common.app.exception.BaseException;
import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class UserException extends BaseException {

  public UserException(ErrorStatus errorStatus) {
    super(errorStatus);
  }
}