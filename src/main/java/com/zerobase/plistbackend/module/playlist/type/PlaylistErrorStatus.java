package com.zerobase.plistbackend.module.playlist.type;

import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PlaylistErrorStatus implements ErrorStatus {
  PLAYLIST_ALREADY_EXIST(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "해당 플레이리스트는 이미 존재합니다"
  );

  private final int errorCode;
  private final String errorType;
  private final String message;

  PlaylistErrorStatus(int errorCode, String errorType, String message) {
    this.errorCode = errorCode;
    this.errorType = errorType;
    this.message = message;
  }
}
