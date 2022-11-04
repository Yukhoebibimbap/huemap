package com.huemap.backend.report.presentation;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.report.application.ReportService;
import com.huemap.backend.report.dto.request.ClosureCreateRequest;
import com.huemap.backend.report.dto.request.PresenceCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/bins")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("{binId}/report-closures")
  public RestResponse saveClosure(
      @PathVariable Long binId,
      @RequestBody @Valid ClosureCreateRequest closureCreateRequest
  ) {
    /**
     * TODO: user 도메인 구현 후 수정 예정
     */
    final Long userId = 1L;
    return RestResponse.of(reportService.saveClosure(userId, binId, closureCreateRequest));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("report-presences")
  public RestResponse savePresence(
      @RequestBody @Valid PresenceCreateRequest presenceCreateRequest
  ) {
    /**
     * TODO: user 도메인 구현 후 수정 예정
     */
    final Long userId = 1L;
    return RestResponse.of(reportService.savePresence(userId, presenceCreateRequest));
  }
}
