package com.zerobase.plistbackend.common.app.exception;

import lombok.Getter;

@Getter
public class JsonParseException extends RuntimeException {
  private final String getMessage;

  public JsonParseException(String getMessage) {
    this.getMessage = getMessage;
  }
}
