package com.huemap.backend.common.exception;

import com.huemap.backend.common.response.error.ErrorCode;

public class EntityNotFoundException extends BusinessException{

	public EntityNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
