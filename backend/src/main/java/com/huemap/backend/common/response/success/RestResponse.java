package com.huemap.backend.common.response.success;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class RestResponse<T> {

  private String message;
  private T data;

  public static <T> RestResponse of(T data) {
    return RestResponse.builder()
        .message("ok")
        .data(data)
        .build();
  }
}
