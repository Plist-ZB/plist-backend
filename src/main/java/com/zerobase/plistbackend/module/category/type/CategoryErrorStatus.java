package com.zerobase.plistbackend.module.category.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CategoryErrorStatus implements ErrorStatus {
  NOT_FOUND(
      HttpStatus.NOT_FOUND.value(),
      HttpStatus.NOT_FOUND.getReasonPhrase(),
      "해당 카테고리는 존재하지 않습니다"
  ),;

  private final int errorCode;
  private final String message;
  private final String errorType;

  CategoryErrorStatus(int errorCode, String message, String errorType) {
    this.errorCode = errorCode;
    this.message = message;
    this.errorType = errorType;
  }
}