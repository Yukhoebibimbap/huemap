package com.huemap.backend.openApi.kakao.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class KakaoMap {

  @JsonProperty("documents")
  private List<KakaoMapAddress> kakaoMapAddresses = new ArrayList<>();

  public KakaoMapRoadAddress getRoadAddress() {
    return kakaoMapAddresses.get(0).getKakaoMapRoadAddress();
  }
}
