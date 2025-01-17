package com.zerobase.plistbackend.common.app.exception;

import com.zerobase.plistbackend.module.category.exception.CategoryException;
import com.zerobase.plistbackend.module.channel.exception.ChannelException;
import com.zerobase.plistbackend.module.home.exception.VideoException;
import com.zerobase.plistbackend.module.user.exception.UserException;
import com.zerobase.plistbackend.module.userplaylist.exception.UserPlaylistException;
import com.zerobase.plistbackend.module.websocket.exception.WebSocketControllerException;
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
