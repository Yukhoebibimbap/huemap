package com.huemap.backend.openApi.kakao;

import static com.huemap.backend.openApi.kakao.KakaoMapEnum.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.openApi.kakao.response.KakaoMap;
import com.huemap.backend.openApi.kakao.response.KakaoMapRoadAddress;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KakaoMapProvider {
  private final WebClient.Builder builder;

  private final String KAKAO_REST_API_KEY = "30216f52aef05b360f528635c4b6796f";

  public KakaoMapRoadAddress getAddressInformation(Double latitude, Double longitude) {
    KakaoMap kakaoMap = requestAddressByCoordinate(latitude, longitude);
    return kakaoMap.getRoadAddress();
  }

  private KakaoMap requestAddressByCoordinate(Double latitude, Double longitude) {
    WebClient webClient = builder.baseUrl(BASE_URL.getValue()).build();
    return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                        .queryParam(COORDINATE_X.getValue(), longitude)
                        .queryParam(COORDINATE_Y.getValue(), latitude)
                        .build())
                    .header(HttpHeaders.AUTHORIZATION, KAKAO_AK.getValue() + KAKAO_REST_API_KEY)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new InvalidValueException(ErrorCode.KAKAO_MAP_REQUEST_INVALID)))
                    .bodyToMono(KakaoMap.class)
                    .block();
  }
}
