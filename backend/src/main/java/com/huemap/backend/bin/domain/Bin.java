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
import com.huemap.backend.openApi.kakao.response.KakaoMapRoadAddress;

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

	private Bin(String gu,
							Point location,
							String address,
							String addressDescription,
							boolean isCandidate,
							BinType type) {
		this.gu = gu;
		this.location = location;
		this.address = address;
		this.addressDescription = addressDescription;
		this.isCandidate = isCandidate;
		this.type = type;
	}

	public static Bin candidateOf(Point location,
																BinType type,
																KakaoMapRoadAddress addressInformation) {
		if (addressInformation == null) {
			final String latitudeLongitudeStr = location.getY() + " " + location.getX();
			return new Bin(latitudeLongitudeStr,
										 location,
										 latitudeLongitudeStr,
										 null,
										 true,
										 type
										 );
		}

		return new Bin(addressInformation.getGu(),
									 location,
									 addressInformation.getAddress(),
									 addressInformation.getAddressDescription(),
									 true,
									 type);
	}

	public void delete() {
		this.deleted = true;
	}

	public void elevate() {
		this.isCandidate = false;
	}
}
