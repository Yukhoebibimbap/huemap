package com.huemap.backend.bin.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.bin.domain.Bin;
import com.huemap.backend.bin.domain.BinRepository;
import com.huemap.backend.bin.domain.BinType;
import com.huemap.backend.bin.domain.mapper.BinDetailMapper;
import com.huemap.backend.bin.domain.mapper.BinMapper;
import com.huemap.backend.bin.dto.response.BinDetailResponse;
import com.huemap.backend.bin.dto.response.BinResponse;
import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.response.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BinService {

	private final BinRepository binRepository;

	public List<BinResponse> findAll(final BinType type) {

		return binRepository.findAllByType(type)
			.stream()
			.map(BinMapper.INSTANCE::toDto)
			.collect(Collectors.toList());
	}

	public BinDetailResponse findById(Long id) {

		Bin bin = binRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.BIN_NOT_FOUND));

		return BinDetailMapper.INSTANCE.toDto(bin);
	}
}
