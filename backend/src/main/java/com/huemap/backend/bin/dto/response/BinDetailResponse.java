package com.huemap.backend.bin.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huemap.backend.bin.domain.Bin;
import com.huemap.backend.bin.domain.BinType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BinDetailResponse {
	private Long id;
	private String gu;
	private Double latitude;
	private Double longitude;
	private String address;
	private String addressDescription;
	private BinType type;
	private LocalDateTime updatedAt;
	private boolean hasClosure;

	@JsonProperty(value = "isCandidate")
	private Boolean isCandidate;

	public static BinDetailResponse toDto(Bin bin, boolean hasClosure) {
		if (bin == null) {
			return null;
		}

		return BinDetailResponse.builder()
			.id(bin.getId())
			.gu(bin.getGu())
			.address(bin.getAddress())
			.addressDescription(bin.getAddressDescription())
			.type(bin.getType())
			.updatedAt(bin.getUpdatedAt())
			.hasClosure(hasClosure)
			.longitude(bin.getLocation().getX())
			.latitude(bin.getLocation().getY())
			.isCandidate(bin.isCandidate())
			.build();
	}
}
