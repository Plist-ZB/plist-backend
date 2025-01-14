package com.zerobase.plistbackend.module.user.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorStatus implements ErrorStatus {

  USER_NOT_FOUND(
      HttpStatus.NOT_FOUND.value(),
      HttpStatus.NOT_FOUND.getReasonPhrase(),
          "요청한 사용자가 없습니다."
          ),

  UPLOAD_IMAGE_FAIL(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
      "파일 업로드가 실패했습니다."
  );

  private final int errorCode;
  private final String errorType;
  private final String message;

  UserErrorStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }

}
