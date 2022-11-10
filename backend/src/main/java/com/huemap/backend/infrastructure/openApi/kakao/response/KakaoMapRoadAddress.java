package com.huemap.backend.infrastructure.openApi.kakao.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoMapRoadAddress {

  @JsonProperty("address_name")
  private String address;

  @JsonProperty("region_2depth_name")
  private String gu;

  @JsonProperty("building_name")
  private String addressDescription;

}
