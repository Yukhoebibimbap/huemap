package com.huemap.backend.bin.dto.response;

import java.time.LocalDateTime;

import com.huemap.backend.bin.domain.BinType;

public class BinDetailResponse {
	private Long id;
	private String gu;
	private Double latitude;
	private Double longitude;
	private String address;
	private String addressDescription;
	private BinType type;
	private LocalDateTime updatedAt;
}
