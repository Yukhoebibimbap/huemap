package com.huemap.backend.bin.domain.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.huemap.backend.bin.domain.Bin;
import com.huemap.backend.bin.dto.response.BinResponse;

@Mapper(
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface BinMapper {
	BinMapper INSTANCE = Mappers.getMapper(BinMapper.class);

	BinResponse toDto(Bin bin, Double latitude, Double longitude);

}
