package com.huemap.backend.bin.dto.response;

import com.huemap.backend.bin.domain.BinType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BinResponse {

	private Long id;
	private Double latitude;
	private Double longitude;
	private BinType type;
}
