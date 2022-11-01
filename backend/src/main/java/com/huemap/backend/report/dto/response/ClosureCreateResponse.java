package com.huemap.backend.report.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClosureCreateResponse {

  private Long id;

  @Builder
  public ClosureCreateResponse(Long id) {
    this.id = id;
  }
}
