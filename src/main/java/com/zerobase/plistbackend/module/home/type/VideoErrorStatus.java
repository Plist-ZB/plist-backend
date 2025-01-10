package com.zerobase.plistbackend.module.home.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum VideoErrorStatus implements ErrorStatus {
  NOT_EXIST(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "해당하는 비디오가 재생목록에 있지 않습니다."
  );


  private final int errorCode;
  private final String errorType;
  private final String message;

  VideoErrorStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }
}