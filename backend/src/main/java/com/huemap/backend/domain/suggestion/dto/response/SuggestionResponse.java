package com.huemap.backend.domain.suggestion.dto.response;

import com.huemap.backend.domain.bin.domain.BinType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuggestionResponse {

	private Long id;
	private String gu;
	private Double latitude;
	private Double longitude;
	private BinType type;

}
