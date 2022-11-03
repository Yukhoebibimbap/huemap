package com.huemap.backend.report.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.bin.domain.Bin;
import com.huemap.backend.bin.domain.BinRepository;
import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.common.utils.GeometryUtil;
import com.huemap.backend.report.domain.Closure;
import com.huemap.backend.report.domain.ReportRepository;
import com.huemap.backend.report.domain.mapper.ClosureMapper;
import com.huemap.backend.report.dto.request.ClosureCreateRequest;
import com.huemap.backend.report.dto.response.ClosureCreateResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;
  private final BinRepository binRepository;

  private final double DISTANCE_METER = 10;

  @Transactional
  public ClosureCreateResponse saveClosure(Long userId,
                                           Long binId,
                                           ClosureCreateRequest closureCreateRequest) {

    final Bin bin = binRepository.findById(binId)
                                 .orElseThrow(
                                     () -> new EntityNotFoundException(ErrorCode.BIN_NOT_FOUND));

    validateDistanceBetweenUserAndBin(bin, closureCreateRequest.getLatitude(),
                                      closureCreateRequest.getLongitude());
    validateAlreadyExist(userId, bin);

    final Closure closure = (Closure)reportRepository.save(ClosureMapper.INSTANCE.toEntity(userId, bin));

    return ClosureMapper.INSTANCE.toDto(closure);
  }

  private void validateAlreadyExist(Long userId, Bin bin) {
    reportRepository.findByUserIdAndBin(userId, bin)
                    .ifPresent(c -> {throw new InvalidValueException(ErrorCode.CLOSURE_DUPLICATED);});
  }

  private void validateDistanceBetweenUserAndBin(Bin bin, Double userLatitude, Double userLongitude) {
    if (GeometryUtil.calculateDistance(bin.getLocation().getX(), bin.getLocation().getY(),
                                       userLatitude, userLongitude) > DISTANCE_METER) {
      throw new InvalidValueException(ErrorCode.REPORT_DISTANCE_FAR);
    }
  }
}
