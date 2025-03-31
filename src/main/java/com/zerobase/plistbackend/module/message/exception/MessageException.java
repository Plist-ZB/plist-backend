package com.zerobase.plistbackend.module.message.exception;

import com.zerobase.plistbackend.common.app.exception.BaseException;
import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class MessageException extends BaseException {

  public MessageException(ErrorStatus errorStatus) {
    super(errorStatus);
  }
}
