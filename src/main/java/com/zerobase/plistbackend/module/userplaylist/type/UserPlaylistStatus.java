package com.zerobase.plistbackend.module.userplaylist.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserPlaylistStatus implements ErrorStatus {
  ALREADY_EXIST(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "해당 제목의 재생목록이 이미 존재합니다."
  );

  private final int errorCode;
  private final String errorType;
  private final String message;

  UserPlaylistStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }
}
