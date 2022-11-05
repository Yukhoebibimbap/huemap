package com.huemap.backend.openApi.kakao.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class KakaoMapAddress {

  @JsonProperty("road_address")
  private KakaoMapRoadAddress kakaoMapRoadAddress = new KakaoMapRoadAddress();
}
