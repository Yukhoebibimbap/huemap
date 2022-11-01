package com.huemap.backend.report.application;

import org.springframework.stereotype.Service;

import com.huemap.backend.report.dto.request.ClosureCreateRequest;
import com.huemap.backend.report.dto.response.ClosureCreateResponse;

@Service
public class ReportService {
  public ClosureCreateResponse saveClosure(Long userId,
                                           Long binId,
                                           ClosureCreateRequest closureCreateRequest) {
    return null;
  }
}
