package com.huemap.backend.common.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INVALID_INPUT_VALUE(400, "적절하지 않은 요청 값입니다."),
	INTERNAL_SERVER_ERROR(500, "서버 내부에 오류가 생겼습니다."),
	BIN_NOT_FOUND(400, "폐수거함을 찾을 수 없습니다."),
	DISTANCE_FAR(400, "폐수거함 대상과 사용자의 거리가 너무 멉니다."),
	CLOSURE_DUPLICATED(400, "중복된 폐수거함 폐쇄 제보를 할 수 없습니다."),
	BIN_DUPLICATED(400, "이미 폐수거함이 존재합니다."),
	KAKAO_MAP_REQUEST_INVALID(400, "카카오맵 OPEN API 요청 값이 적절하지 않습니다."),
	USER_EMAIL_DUPLICATED(400, "중복된 이메일입니다."),
	USER_NOT_FOUND(400, "사용자를 찾을 수 없습니다."),
	LOGIN_PASSWORD_NOT_MATCH(400, "비밀번호가 일치하지 않습니다."),
	AUTH_ERROR(400,  "인증 관련 오류가 발생했습니다."),
	BAD_LOGIN(400, "잘못된 아이디 또는 패스워드입니다."),

	SUGGESTION_LIMIT_EXCEEDED(400, "하루 제안 횟수를 초과하였습니다"),
	PRESENCE_NOT_FOUND(400, "존재 폐수거함 제보를 찾을 수 없습니다.");

	private final int status;
	private final String message;
}
