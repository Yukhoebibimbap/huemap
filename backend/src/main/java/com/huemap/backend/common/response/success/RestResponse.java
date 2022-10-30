package com.huemap.backend.common.response.success;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RestResponse<T> {

  private final String message;
  private final T data;

  public static <T> RestResponse of(T data) {
    return RestResponse.builder()
        .message("ok")
        .data(data)
        .build();
  }
}
