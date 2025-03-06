package com.zerobase.plistbackend.module.home.exception;

import com.zerobase.plistbackend.common.app.exception.BaseException;
import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class VideoException extends BaseException {

  public VideoException(ErrorStatus errorStatus) {
    super(errorStatus);
  }
}