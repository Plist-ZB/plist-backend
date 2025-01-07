package com.zerobase.plistbackend.common.app.exception;

import com.zerobase.plistbackend.module.channel.exception.ChannelException;
import com.zerobase.plistbackend.module.user.exception.OAuth2UserExceptionm;
import com.zerobase.plistbackend.module.userplaylist.exception.UserPlaylistException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

  @ExceptionHandler(OAuth2UserExceptionm.class)
  public ErrorResponse handleOAuth2UserException(OAuth2UserExceptionm e) {
    return ErrorResponse.create(e.getErrorStatus());
  }

}
