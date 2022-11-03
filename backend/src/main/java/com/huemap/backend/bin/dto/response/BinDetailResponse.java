package com.huemap.backend.bin.dto.response;

import java.time.LocalDateTime;

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
	private int closureCount;
}
