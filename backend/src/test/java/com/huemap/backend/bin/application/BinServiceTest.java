package com.huemap.backend.bin.application;

import static com.huemap.backend.common.TestUtils.getBin;
import static com.huemap.backend.common.utils.GeometryUtil.convertPoint;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import java.util.List;
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
import com.huemap.backend.bin.dto.response.BinDetailResponse;
import com.huemap.backend.bin.dto.response.BinResponse;
import com.huemap.backend.bin.event.BinCreateEvent;
import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.openApi.kakao.KakaoMapProvider;
import com.huemap.backend.report.domain.ReportRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("BinService의")
public class BinServiceTest {

  @InjectMocks
  private BinService binService;

  @Mock
  private BinRepository binRepository;

  @Mock
  private ReportRepository reportRepository;

  @Mock
  private KakaoMapProvider kakaoMapProvider;

  @Nested
  @DisplayName("findAll 메소드는")
  class findAll {

    @Nested
    @DisplayName("올바른 폐수거함 type이 넘어오면")
    class Context_with_valid_bin_type {

      @Test
      @DisplayName("해당 type의 폐수거함을 모두 조회한다")
      void success() throws Exception {
        //given
        final BinType type = BinType.CLOTHES;

        final List<Bin> bins = List.of(getBin());
        given(binRepository.findAllByType(any(BinType.class))).willReturn(bins);

        //when
        final List<BinResponse> foundBins = binService.findAll(type);

        //then
        assertThat(foundBins.size()).isEqualTo(1);
        assertThat(foundBins.get(0).getType()).isEqualTo(type);
      }
    }

  }

  @Nested
  @DisplayName("findById 메소드는")
  class findById {

    @Nested
    @DisplayName("올바른 폐수거함 id가 넘어오면")
    class Context_with_valid_id {

      @Test
      @DisplayName("해당 id의 폐수거함 정보를 조회한다")
      void success() throws Exception {
        //given
        final Long id = 1L;

        final Bin bin = getBin();
        given(binRepository.findById(id)).willReturn(Optional.of(bin));
        given(reportRepository.countClosureByBin(anyLong())).willReturn(anyInt());

        //when
        final BinDetailResponse foundBin = binService.findById(id);

        //then
        verify(binRepository).findById(id);
        verify(reportRepository).countClosureByBin(anyLong());
        assertThat(foundBin.getAddress()).isEqualTo("서울특별시 종로구 창덕궁7길 5");
      }
    }

    @Nested
    @DisplayName("존재하지 않은 폐수거함 id가 넘어오면")
    class Context_with_invalid_id {

      @Test
      @DisplayName("예외를 던진다")
      void It_throws_exception() throws Exception {
        //given
        final Long id = 2L;
        given(binRepository.findById(id)).willReturn(Optional.empty());

        //when //then
        assertThatThrownBy(
            () -> binService.findById(id))
            .isInstanceOf(EntityNotFoundException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.BIN_NOT_FOUND);
      }
    }

  }

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
