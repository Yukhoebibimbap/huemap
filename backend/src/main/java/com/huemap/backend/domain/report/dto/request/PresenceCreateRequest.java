package com.huemap.backend.domain.report.dto.request;

import javax.validation.constraints.NotNull;

import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.common.validator.LatitudeValid;
import com.huemap.backend.common.validator.LongitudeValid;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PresenceCreateRequest {

  @NotNull
  private BinType type;

  @NotNull
  @LatitudeValid
  private Double latitude;

  @NotNull
  @LongitudeValid
  private Double longitude;
}
