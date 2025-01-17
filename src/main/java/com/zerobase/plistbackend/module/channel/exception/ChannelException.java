package com.zerobase.plistbackend.module.channel.exception;

import com.zerobase.plistbackend.common.app.exception.BaseException;
import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class ChannelException extends BaseException {

  public ChannelException(ErrorStatus errorStatus) {
    super(errorStatus);
  }
}
