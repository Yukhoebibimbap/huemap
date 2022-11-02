package com.huemap.backend.bin.domain.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

	@Mapping(expression = "java(bin.getLocation().getX())", target="latitude")
	@Mapping(expression = "java(bin.getLocation().getY())", target="longitude")
	BinResponse toDto(Bin bin);

}
