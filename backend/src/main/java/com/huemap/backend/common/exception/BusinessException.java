package com.huemap.backend.common.exception;

import com.huemap.backend.common.response.error.ErrorCode;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {
	private final ErrorCode errorCode;

	protected BusinessException(final ErrorCode errorCode) {
		this.errorCode=errorCode;
	}
}
