package com.huemap.backend.domain.report.application;

import static com.huemap.backend.common.utils.GeometryUtil.convertPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Point;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.common.utils.GeometryUtil;
import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.domain.bin.domain.BinRepository;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.bin.domain.ConditionType;
import com.huemap.backend.domain.bin.event.BinCreateEvent;
import com.huemap.backend.domain.report.domain.Closure;
import com.huemap.backend.domain.report.domain.Condition;
import com.huemap.backend.domain.report.domain.Image;
import com.huemap.backend.domain.report.domain.Presence;
import com.huemap.backend.domain.report.domain.ReportRepository;
import com.huemap.backend.domain.report.domain.mapper.ClosureMapper;
import com.huemap.backend.domain.report.domain.mapper.ConditionMapper;
import com.huemap.backend.domain.report.domain.mapper.PresenceMapper;
import com.huemap.backend.domain.report.dto.request.ClosureCreateRequest;
import com.huemap.backend.domain.report.dto.request.ConditionCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceVoteRequest;
import com.huemap.backend.domain.report.dto.response.ClosureCreateResponse;
import com.huemap.backend.domain.report.dto.response.ConditionCreateResponse;
import com.huemap.backend.domain.report.dto.response.ConditionResponse;
import com.huemap.backend.domain.report.dto.response.PresenceCreateResponse;
import com.huemap.backend.infrastructure.s3.S3Uploader;
import com.huemap.backend.infrastructure.socket.SocketService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;
  private final BinRepository binRepository;
  private final ApplicationEventPublisher publisher;
  private final S3Uploader s3Uploader;

  private final SocketService socketService;
  private final SocketIOServer socketIOServer;

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

  @Transactional
  public ConditionCreateResponse saveCondition(Long userId,
                                               Long binId,
                                               ConditionCreateRequest conditionCreateRequest,
                                               MultipartFile multipartFile) throws IOException {
    final Bin bin = binRepository.findById(binId)
                                 .orElseThrow(
                                     () -> new EntityNotFoundException(ErrorCode.BIN_NOT_FOUND));

    validateDistanceBetweenUserAndBin(bin, conditionCreateRequest.getLatitude(),
                                      conditionCreateRequest.getLongitude());

    final Image image = new Image(s3Uploader.upload(multipartFile));
    final Condition condition = (Condition)reportRepository.save(
        ConditionMapper.INSTANCE.toEntity(userId, bin, image, conditionCreateRequest.getType()));

    sendSocketMessage(condition);

    return ConditionMapper.INSTANCE.toDto(condition);
  }

  public List<ConditionResponse> findAllConditionByGuAndType(String gu, ConditionType type, LocalDateTime startDate,
      LocalDateTime endDate) {

    return (List<ConditionResponse>)reportRepository.findAllConditionByGuAndTypeAndCreatedAtBetween(gu, type, startDate,
            endDate)
        .stream().map(e -> ConditionResponse.toDto((Condition)e)).collect(Collectors.toList());
  }

  private void sendSocketMessage(Condition condition) {

    ConditionResponse response = ConditionResponse.toDto(condition);

    for( SocketIOClient client :socketIOServer.getAllClients()){
      socketService.sendSocketMessage(client, response, response.getGu());
    }
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
