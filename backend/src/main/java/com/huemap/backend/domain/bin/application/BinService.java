package com.huemap.backend.domain.bin.application;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.domain.bin.domain.BinRepository;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.bin.dto.response.BinDetailResponse;
import com.huemap.backend.domain.bin.dto.response.BinResponse;
import com.huemap.backend.domain.bin.event.BinCreateEvent;
import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.infrastructure.openApi.kakao.KakaoMapProvider;
import com.huemap.backend.infrastructure.openApi.kakao.response.KakaoMapRoadAddress;
import com.huemap.backend.domain.report.domain.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BinService {

	private final BinRepository binRepository;
	private final ReportRepository reportRepository;
	private final KakaoMapProvider kakaoMapProvider;

	public List<BinResponse> findAll(final BinType type) {

		return binRepository.findAllByType(type)
			.stream()
			.map(BinResponse::toDto)
			.collect(Collectors.toList());
	}

	public BinDetailResponse findById(Long id) {

		Bin bin = binRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.BIN_NOT_FOUND));

		int count = reportRepository.countClosureByBin(id);

		return BinDetailResponse.toDto(bin, count != 0);
	}

	public void save(BinCreateEvent binCreateEvent) {
		validateCandidateBinAlreadyExist(binCreateEvent.getType(), binCreateEvent.getLocation());

		final KakaoMapRoadAddress addressInformation = kakaoMapProvider.getAddressInformation(
				binCreateEvent.getLocation().getY(), binCreateEvent.getLocation().getX());

		binRepository.save(Bin.candidateOf(binCreateEvent.getLocation(),
																			 binCreateEvent.getType(),
																			 addressInformation));
	}

	private void validateCandidateBinAlreadyExist(BinType type, Point location) {
		binRepository.findCandidateBinByTypeAndLocation(type, location)
								 .ifPresent(b -> {
									 throw new InvalidValueException(ErrorCode.CANDIDATE_BIN_DUPLICATED);
								 });
	}
}
