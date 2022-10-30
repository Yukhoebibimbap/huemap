package com.huemap.backend.bin.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.huemap.backend.common.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table
public class Bin extends BaseEntity {

	@NotNull
	private String gu;

	@NotNull
	private Double latitude;

	@NotNull
	private Double longitude;

	@NotNull
	private String address;

	private String addressDescription;

	@Enumerated(EnumType.STRING)
	private BinType binType;

}
