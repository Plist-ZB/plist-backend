package com.zerobase.plistbackend.module.userplaylist.exception;

import com.zerobase.plistbackend.common.app.exception.BaseException;
import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class UserPlaylistException extends BaseException {

  public UserPlaylistException(ErrorStatus errorStatus) {
    super(errorStatus);
  }
}
