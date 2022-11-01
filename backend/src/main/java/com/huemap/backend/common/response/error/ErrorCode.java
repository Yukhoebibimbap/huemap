package com.huemap.backend.common.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INVALID_INPUT_VALUE(400, "적절하지 않은 요청 값입니다."),
	INTERNAL_SERVER_ERROR(500, "서버 내부에 오류가 생겼습니다."),
	BIN_NOT_FOUND(400, "폐수거함이 존재하지 않습니다.");

	private final int status;
	private final String message;
}
