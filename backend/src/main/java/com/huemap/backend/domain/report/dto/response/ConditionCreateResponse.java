package com.huemap.backend.domain.report.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConditionCreateResponse {
  private Long id;

  @Builder
  public ConditionCreateResponse(Long id) {
    this.id = id;
  }
}
