package com.huemap.backend.report.dto.request;

import javax.validation.constraints.NotNull;

import com.huemap.backend.common.validator.LatitudeValid;
import com.huemap.backend.common.validator.LongitudeValid;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClosureCreateRequest {

  @NotNull
  @LatitudeValid
  private Double latitude;

  @NotNull
  @LongitudeValid
  private Double longitude;
}
