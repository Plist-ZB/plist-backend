package com.zerobase.plistbackend.module.websocket.exception;

import com.zerobase.plistbackend.common.app.exception.BaseException;
import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class WebSocketControllerException extends BaseException {

  public WebSocketControllerException(ErrorStatus errorStatus) {
    super(errorStatus);
  }
}
