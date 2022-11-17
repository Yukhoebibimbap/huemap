package com.huemap.backend.common.exception;

import com.huemap.backend.common.response.error.ErrorCode;

import lombok.Getter;

@Getter
public class AuthException extends BusinessException {

  public AuthException(ErrorCode errorCode) {
    super(errorCode);
  }

}
