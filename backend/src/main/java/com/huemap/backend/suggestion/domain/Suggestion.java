package com.huemap.backend.suggestion.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.locationtech.jts.geom.Point;

import com.huemap.backend.bin.domain.BinType;
import com.huemap.backend.common.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table
@Builder
@AllArgsConstructor
public class Suggestion extends BaseEntity {

	@NotNull
	private String gu;

	@Column(columnDefinition = "GEOMETRY")
	private Point location;

	@Enumerated(EnumType.STRING)
	private BinType type;

	@NotNull
	private Long userId;
}
