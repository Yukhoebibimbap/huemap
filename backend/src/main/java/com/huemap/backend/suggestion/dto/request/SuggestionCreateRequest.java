package com.huemap.backend.suggestion.dto.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.huemap.backend.bin.domain.BinType;
import com.huemap.backend.common.validator.LatitudeValid;
import com.huemap.backend.common.validator.LongitudeValid;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuggestionCreateRequest {
	@NotNull
	private String gu;

	@NotNull
	@LatitudeValid
	private Double latitude;

	@NotNull
	@LongitudeValid
	private Double longitude;

	@Enumerated(EnumType.STRING)
	private BinType type;
}
