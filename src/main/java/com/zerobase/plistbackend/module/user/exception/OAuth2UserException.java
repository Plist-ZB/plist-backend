package com.zerobase.plistbackend.module.user.exception;

import com.zerobase.plistbackend.common.app.exception.BaseException;
import com.zerobase.plistbackend.common.app.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class OAuth2UserException extends BaseException {

  public OAuth2UserException(ErrorStatus errorStatus) {
      super(errorStatus);
    }
}
