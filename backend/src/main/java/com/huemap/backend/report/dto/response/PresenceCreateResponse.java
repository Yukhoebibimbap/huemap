package com.huemap.backend.report.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class PresenceCreateResponse {

  private Long id;

  @Builder
  public PresenceCreateResponse(Long id) {
    this.id = id;
  }
}