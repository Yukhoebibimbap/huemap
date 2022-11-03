package com.huemap.backend.common.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INVALID_INPUT_VALUE(400, "적절하지 않은 요청 값입니다."),
	INTERNAL_SERVER_ERROR(500, "서버 내부에 오류가 생겼습니다."),
	BIN_NOT_FOUND(400, "폐수거함을 찾을 수 없습니다."),
	REPORT_DISTANCE_FAR(400, "제보하려는 대상과 사용자의 거리가 너무 멉니다."),
	CLOSURE_DUPLICATED(400, "중복된 폐수거함 폐쇄 제보를 할 수 없습니다.");

	private final int status;
	private final String message;
}
