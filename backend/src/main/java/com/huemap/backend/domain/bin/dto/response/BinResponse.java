package com.huemap.backend.domain.bin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.domain.bin.domain.BinType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BinResponse {

	private Long id;
	private Double latitude;
	private Double longitude;
	private BinType type;

	@JsonProperty(value = "isCandidate")
	private Boolean isCandidate;

	public static BinResponse toDto(Bin bin) {
		if (bin == null) {
			return null;
		}

		return BinResponse.builder()
				.id(bin.getId())
				.type(bin.getType())
				.longitude(bin.getLocation().getX())
				.latitude(bin.getLocation().getY())
				.isCandidate(bin.isCandidate())
				.build();
	}
}
