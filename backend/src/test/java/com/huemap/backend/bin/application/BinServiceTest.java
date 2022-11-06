package com.huemap.backend.bin.application;

import static com.huemap.backend.common.TestUtils.*;
import static com.huemap.backend.common.utils.GeometryUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.huemap.backend.bin.domain.Bin;
import com.huemap.backend.bin.domain.BinRepository;
import com.huemap.backend.bin.domain.BinType;
import com.huemap.backend.bin.event.BinCreateEvent;
import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.openApi.kakao.KakaoMapProvider;
import com.huemap.backend.openApi.kakao.response.KakaoMapRoadAddress;

@ExtendWith(MockitoExtension.class)
@DisplayName("BinService의")
public class BinServiceTest {

  @InjectMocks
  private BinService binService;

  @Mock
  private BinRepository binRepository;

  @Mock
  private KakaoMapProvider kakaoMapProvider;

  @Nested
  @DisplayName("save 메소드")
  class save {

    @Nested
    @DisplayName("이미 존재하는 폐수거함을 저장할려고 한다면")
    class Context_with_already_exist_bin {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final BinCreateEvent event = BinCreateEvent.candidateOf(BinType.CLOTHES,
                                                                convertPoint(37.583297,
                                                                             126.987755));
        final Bin bin = getBin();
        given(binRepository.findCandidateBinByTypeAndLocation(any(BinType.class),
                                                              any(Point.class)))
            .willReturn(Optional.of(bin));

        // when, then
        assertThatThrownBy(
            () -> binService.save(event))
            .isInstanceOf(InvalidValueException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.CANDIDATE_BIN_DUPLICATED);
      }
    }

    @Nested
    @DisplayName("유효한 값이 넘어오면")
    class Context_with_valid_argument {

      @Test
      @DisplayName("폐수거함을 저장한다.")
      void success() throws Exception {
        //given
        final BinCreateEvent event = BinCreateEvent.candidateOf(BinType.CLOTHES,
                                                                convertPoint(37.583297,
                                                                             126.987755));
        final Bin bin = getBin();
        given(binRepository.findCandidateBinByTypeAndLocation(any(BinType.class),
                                                              any(Point.class)))
            .willReturn(Optional.empty());
        given(binRepository.save(any(Bin.class))).willReturn(bin);

        //when
        binService.save(event);

        //then
        verify(binRepository).save(any(Bin.class));
      }
    }
  }
}
