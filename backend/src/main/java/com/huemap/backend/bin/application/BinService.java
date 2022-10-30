package com.huemap.backend.bin.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.bin.dto.response.BinResponse;
import com.huemap.backend.bin.dto.response.BinResponses;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BinService {

	public BinResponses findAll() {

		List<BinResponse> binResponses = null;

		return new BinResponses(binResponses);
	}
}
