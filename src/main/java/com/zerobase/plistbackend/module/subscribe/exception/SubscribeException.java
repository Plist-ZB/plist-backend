package com.zerobase.plistbackend.module.subscribe.exception;

import com.zerobase.plistbackend.common.app.exception.BaseException;
import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class SubscribeException extends BaseException {

  public SubscribeException(ErrorStatus errorStatus) {
    super(errorStatus);
  }
}