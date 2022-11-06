package com.huemap.backend.suggestion.exception;

import com.huemap.backend.common.exception.BusinessException;
import com.huemap.backend.common.response.error.ErrorCode;

public class SuggestionLimitExceededException extends BusinessException {

	public SuggestionLimitExceededException() {
		super(ErrorCode.SUGGESTION_LIMIT_EXCEEDED);
	}
}
