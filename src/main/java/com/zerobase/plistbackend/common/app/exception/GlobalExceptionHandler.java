package com.zerobase.plistbackend.common.app.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseException.class)
  public ErrorResponse handleBaseException(BaseException e) {
    log.error("Error occurred: {} - {} - {}", e.getErrorStatus().getErrorCode(),
        e.getErrorStatus().getErrorType(), e.getErrorStatus().getMessage());
    return ErrorResponse.create(e.getErrorStatus());
  }
}
