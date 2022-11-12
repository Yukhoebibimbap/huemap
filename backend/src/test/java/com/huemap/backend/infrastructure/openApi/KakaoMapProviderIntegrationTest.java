package com.huemap.backend.infrastructure.openApi;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.huemap.backend.infrastructure.openApi.kakao.KakaoMapProvider;
import com.huemap.backend.infrastructure.openApi.kakao.response.KakaoMapRoadAddress;

@SpringBootTest
@DisplayName("KakaoMapProvider의")
public class KakaoMapProviderIntegrationTest {

  static {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
  }

  @Autowired
  private KakaoMapProvider kakaoMapProvider;

  @Nested
  @DisplayName("getAddressInformation 메소드는 위도와 경도 값이 주어지면")
  class Context_with_latitude_longitude {

    @Test
    @DisplayName("Kakao Map OPEN API를 사용하여 해당 위치에 대한 도로명 주소를 응답한다.")
   void It_response_address() {
      //given
      final Double latitude = 37.5825627;
      final Double longitude = 127.059971;
      final String address = "서울특별시 동대문구 서울시립대로 163";
      final String gu = "동대문구";
      final String addressDescription = "서울시립대학교";

      //when
      final KakaoMapRoadAddress addressInformation = kakaoMapProvider.getAddressInformation(latitude,
                                                                                            longitude);

      //then
      assertThat(addressInformation.getAddress()).isEqualTo(address);
      assertThat(addressInformation.getGu()).isEqualTo(gu);
      assertThat(addressInformation.getAddressDescription()).isEqualTo(addressDescription);
    }
  }
}
