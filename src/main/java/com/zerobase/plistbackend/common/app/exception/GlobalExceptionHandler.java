package com.zerobase.plistbackend.common.app.exception;

import com.zerobase.plistbackend.module.category.exception.CategoryException;
import com.zerobase.plistbackend.module.channel.exception.ChannelException;
import com.zerobase.plistbackend.module.home.exception.VideoException;
import com.zerobase.plistbackend.module.playlist.exception.PlaylistException;
import com.zerobase.plistbackend.module.user.exception.UserException;
import com.zerobase.plistbackend.module.userplaylist.exception.UserPlaylistException;
import com.zerobase.plistbackend.module.websocket.exception.WebSocketControllerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(JsonParseException.class)
  public ErrorResponse handleJsonParseException(JsonParseException e) {
    return ErrorResponse.create(e.getErrorStatus());
  }

  @ExceptionHandler(UserPlaylistException.class)
  public ErrorResponse handleJsonParseException(UserPlaylistException e) {
    return ErrorResponse.create(e.getErrorStatus());
  }

  @ExceptionHandler(ChannelException.class)
  public ErrorResponse handleChannelException(ChannelException e) {
    return ErrorResponse.create(e.getErrorStatus());
  }

  @ExceptionHandler(UserException.class)
  public ErrorResponse handleUserException(UserException e) {
    log.error("UserException", e);
    return ErrorResponse.create(e.getErrorStatus());
  }

  @ExceptionHandler(CategoryException.class)
  public ErrorResponse handleCategoryException(CategoryException e) {
    return ErrorResponse.create(e.getErrorStatus());
  }

  @ExceptionHandler(PlaylistException.class)
  public ErrorResponse handlePlaylistException(PlaylistException e) {
    return ErrorResponse.create(e.getErrorStatus());
  }

  @ExceptionHandler(VideoException.class)
  public ErrorResponse handleVideoException(VideoException e) {
    return ErrorResponse.create(e.getErrorStatus());
  }

  @ExceptionHandler(WebSocketControllerException.class)
  public ErrorResponse handleWebSocketControllerException(WebSocketControllerException e) {
    return ErrorResponse.create(e.getErrorStatus());
  }
}
