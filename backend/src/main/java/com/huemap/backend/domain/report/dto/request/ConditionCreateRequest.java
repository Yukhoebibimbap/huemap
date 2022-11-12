package com.huemap.backend.domain.report.dto.request;

import javax.validation.constraints.NotNull;

import com.huemap.backend.common.validator.LatitudeValid;
import com.huemap.backend.common.validator.LongitudeValid;
import com.huemap.backend.domain.bin.domain.ConditionType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConditionCreateRequest {

  @NotNull
  private ConditionType type;

  @NotNull
  @LatitudeValid
  private Double latitude;

  @NotNull
  @LongitudeValid
  private Double longitude;
}
