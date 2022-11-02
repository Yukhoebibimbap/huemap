package com.huemap.backend.bin.domain.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.huemap.backend.bin.domain.Bin;
import com.huemap.backend.bin.dto.response.BinDetailResponse;

@Mapper(
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface BinDetailMapper {
	BinDetailMapper INSTANCE = Mappers.getMapper(BinDetailMapper.class);

	BinDetailResponse toDto(Bin bin, Double latitude, Double longitude);
}
