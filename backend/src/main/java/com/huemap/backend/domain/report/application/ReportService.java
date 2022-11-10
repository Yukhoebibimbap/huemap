package com.huemap.backend.domain.report.application;

import static com.huemap.backend.common.utils.GeometryUtil.*;

import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.domain.bin.domain.BinRepository;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.bin.event.BinCreateEvent;
import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.common.utils.GeometryUtil;
import com.huemap.backend.domain.report.domain.Closure;
import com.huemap.backend.domain.report.domain.Presence;
import com.huemap.backend.domain.report.domain.ReportRepository;
import com.huemap.backend.domain.report.domain.mapper.ClosureMapper;
import com.huemap.backend.domain.report.domain.mapper.PresenceMapper;
import com.huemap.backend.domain.report.dto.request.ClosureCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceVoteRequest;
import com.huemap.backend.domain.report.dto.response.ClosureCreateResponse;
import com.huemap.backend.domain.report.dto.response.PresenceCreateResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;
  private final BinRepository binRepository;
  private final ApplicationEventPublisher publisher;

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
    validateClosureAlreadyExist(userId, bin);

    final Closure closure = (Closure)reportRepository.save(
        ClosureMapper.INSTANCE.toEntity(userId, bin));

    return ClosureMapper.INSTANCE.toDto(closure);
  }

  @Transactional
  public PresenceCreateResponse savePresence(Long userId, PresenceCreateRequest presenceCreateRequest) {
    final BinType type = presenceCreateRequest.getType();
    final Point location = convertPoint(presenceCreateRequest.getLatitude(),
                                        presenceCreateRequest.getLongitude());
    publisher.publishEvent(BinCreateEvent.candidateOf(type, location));

    final Bin bin = binRepository.findCandidateBinByTypeAndLocation(type, location)
                                 .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BIN_NOT_FOUND));

    final Presence presence = (Presence)reportRepository.save(Presence.of(userId, bin));

    return PresenceMapper.INSTANCE.toDto(presence);
  }

  @Transactional
  public void votePresence(Long binId, PresenceVoteRequest presenceVoteRequest) {
    final Bin bin = binRepository.findById(binId)
                                 .orElseThrow(
                                     () -> new EntityNotFoundException(ErrorCode.BIN_NOT_FOUND));

    validateDistanceBetweenUserAndBin(bin, presenceVoteRequest.getLatitude(),
                                      presenceVoteRequest.getLongitude());

    final Optional<Presence> presence = reportRepository.findPresenceByBinAndDeletedFalse(bin);
    if (presence.isEmpty()) {
      throw new EntityNotFoundException(ErrorCode.PRESENCE_NOT_FOUND);
    }

    presence.get().addCount();
  }

  private void validateClosureAlreadyExist(Long userId, Bin bin) {
    reportRepository.findClosureByUserIdAndBin(userId, bin)
                    .ifPresent(c -> {throw new InvalidValueException(ErrorCode.CLOSURE_DUPLICATED);});
  }

  private void validateDistanceBetweenUserAndBin(Bin bin, Double userLatitude, Double userLongitude) {
    if (GeometryUtil.calculateDistance(bin.getLocation().getY(), bin.getLocation().getX(),
                                       userLatitude, userLongitude) > DISTANCE_METER) {
      throw new InvalidValueException(ErrorCode.DISTANCE_FAR);
    }
  }
}
