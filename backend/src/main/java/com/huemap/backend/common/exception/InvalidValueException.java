package com.huemap.backend.common.exception;

import com.huemap.backend.common.response.error.ErrorCode;

public class InvalidValueException extends BusinessException {

  public InvalidValueException(ErrorCode errorCode) {
    super(errorCode);
  }
}
