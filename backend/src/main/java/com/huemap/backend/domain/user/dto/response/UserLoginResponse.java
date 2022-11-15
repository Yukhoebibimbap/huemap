package com.huemap.backend.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginResponse {
  private Long id;

  @Builder
  public UserLoginResponse(Long id) {
    this.id = id;
  }
}
