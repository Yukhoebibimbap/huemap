package com.huemap.backend.bin.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.bin.domain.mapper.BinMapper;
import com.huemap.backend.bin.domain.BinRepository;
import com.huemap.backend.bin.domain.BinType;
import com.huemap.backend.bin.dto.response.BinResponse;

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

	public BinResponse findById(Long binId) {
		return null;
	}
}
