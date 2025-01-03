package com.zerobase.plistbackend.common.app.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JsonErrorStatus implements ErrorStatus {
  SEVER_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
      "JSON 데이터 파싱 or URL 네트워크 에러 발생!!"
  );


  private final int errorCode;
  private final String errorType;
  private final String message;

  JsonErrorStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }
}
