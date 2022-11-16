package com.huemap.backend.domain.report.dto.response;

import com.huemap.backend.domain.bin.domain.ConditionType;
import com.huemap.backend.domain.report.domain.Condition;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConditionResponse {

	private String gu;
	private ConditionType type;
	private String img;
	private Double latitude;
	private Double longitude;
	private String createdAt;


	public static ConditionResponse toDto(Condition condition) {
		if (condition == null || condition.getBin() ==null || condition.getCreatedAt() ==null) {
			return null;
		}

		return ConditionResponse.builder()
			.img(condition.getImage().getImgUrl())
			.gu(condition.getBin().getGu())
			.type(condition.getType())
			.latitude(condition.getBin().getLocation().getY())
			.longitude(condition.getBin().getLocation().getX())
			.createdAt(condition.getCreatedAt().toString())
			.build();
	}
}
