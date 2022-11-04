package com.huemap.backend.bin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;

import com.huemap.backend.common.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table
@Where(clause = "deleted=false")
public class Bin extends BaseEntity {

	@NotNull
	private String gu;

	@Column(columnDefinition = "GEOMETRY")
	private Point location;

	@NotNull
	private String address;

	private String addressDescription;

	@Enumerated(EnumType.STRING)
	private BinType type;

	private boolean isCandidate;

	private boolean deleted;

	public void delete() {
		this.deleted = true;
	}
}
