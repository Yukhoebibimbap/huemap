package com.huemap.backend.bin.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BinType {
	GENERAL,
	RECYCLE,
	CLOTHES,
	BATTERY,
	MEDICINE,
	LAMP;

	@JsonCreator
	public static BinType from(String name) {
		for (BinType type : BinType.values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}

		return null;
	}
}
