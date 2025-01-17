package com.zerobase.plistbackend.common.app.exception;

import lombok.Getter;

@Getter
public class JsonParseException extends BaseException {

  public JsonParseException(ErrorStatus errorStatus) {
    super(errorStatus);
  }
}
