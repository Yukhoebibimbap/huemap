package com.huemap.backend.domain.suggestion.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuggestionCreateResponse {
	private Long id;

	@Builder
	public SuggestionCreateResponse(Long id) {
		this.id = id;
	}
}
