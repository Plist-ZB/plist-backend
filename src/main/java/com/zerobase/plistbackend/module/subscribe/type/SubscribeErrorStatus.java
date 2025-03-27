package com.zerobase.plistbackend.module.subscribe.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SubscribeErrorStatus implements ErrorStatus {

  SUBSCRIBE_NOT_FOUND(
      HttpStatus.NOT_FOUND.value(),
      HttpStatus.NOT_FOUND.getReasonPhrase(),
      "구독정보가 존재하지 않습니다."
  ),
  SUBSCRIBE_ALREADY_EXIST(
      HttpStatus.ALREADY_REPORTED.value(),
      HttpStatus.ALREADY_REPORTED.getReasonPhrase(),
      "이미 구독한 사용자입니다."
  );

  private final int errorCode;
  private final String errorType;
  private final String message;

  SubscribeErrorStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }
}