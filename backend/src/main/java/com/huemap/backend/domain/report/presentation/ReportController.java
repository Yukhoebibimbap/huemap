package com.huemap.backend.domain.report.presentation;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.common.security.CurrentUser;
import com.huemap.backend.domain.bin.domain.ConditionType;
import com.huemap.backend.domain.report.application.ReportService;
import com.huemap.backend.domain.report.dto.request.ClosureCreateRequest;
import com.huemap.backend.domain.report.dto.request.ConditionCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceVoteRequest;
import com.huemap.backend.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/bins")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("{binId}/report-closures")
  public RestResponse saveClosure(
      @CurrentUser User user,
      @PathVariable Long binId,
      @RequestBody @Valid ClosureCreateRequest closureCreateRequest
  ) {
    return RestResponse.of(reportService.saveClosure(user.getId(), binId, closureCreateRequest));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("report-presences")
  public RestResponse savePresence(
      @CurrentUser User user,
      @RequestBody @Valid PresenceCreateRequest presenceCreateRequest
  ) {
    return RestResponse.of(reportService.savePresence(user.getId(), presenceCreateRequest));
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping("{binId}/vote")
  public void votePresence(
      @PathVariable Long binId,
      @RequestBody @Valid PresenceVoteRequest presenceVoteRequest
  ) {
    reportService.votePresence(binId, presenceVoteRequest);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(
      value = "{binId}/report-condition",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public RestResponse saveCondition(
      @CurrentUser User user,
      @PathVariable Long binId,
      @RequestPart(value = "dto") @Valid ConditionCreateRequest conditionCreateRequest,
      @RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
    return RestResponse.of(
        reportService.saveCondition(user.getId(), binId, conditionCreateRequest, multipartFile));
  }

  @GetMapping("report-condition")
  public ResponseEntity<RestResponse> findAllConditionByGuAndType(@PathParam("gu") String gu, @PathParam("type") ConditionType type,
      @PathParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,
      @PathParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate) {

    RestResponse response = RestResponse.of(reportService.findAllConditionByGuAndType(gu, type, startDate, endDate));
    return ResponseEntity.ok(response);
  }
}
