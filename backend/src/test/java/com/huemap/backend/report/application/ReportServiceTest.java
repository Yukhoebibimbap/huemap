package com.huemap.backend.report.application;

import static com.huemap.backend.common.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.huemap.backend.bin.domain.Bin;
import com.huemap.backend.bin.domain.BinRepository;
import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.report.domain.Closure;
import com.huemap.backend.report.domain.ReportRepository;
import com.huemap.backend.report.dto.request.ClosureCreateRequest;
import com.huemap.backend.report.dto.response.ClosureCreateResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReportService의")
public class ReportServiceTest {

  @InjectMocks
  private ReportService reportService;

  @Mock
  private ReportRepository reportRepository;

  @Mock
  private BinRepository binRepository;

  @Nested
  @DisplayName("saveClosure 메소드는")
  class saveClosure {

    @Nested
    @DisplayName("폐수거함이 존재하지 않는 경우")
    class Context_with_not_found_bin {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() {
        //given
        final ClosureCreateRequest request = new ClosureCreateRequest(37.583297, 126.987755);
        given(binRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(
            () -> reportService.saveClosure(1L, 1L, request))
            .isInstanceOf(EntityNotFoundException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.BIN_NOT_FOUND);
      }
    }

    @Nested
    @DisplayName("폐쇄 제보하려는 위치와 폐수거함의 거리 차이가 10m 이상이면")
    class Context_with_distance_far_bin {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final ClosureCreateRequest request = new ClosureCreateRequest(37.583289, 126.987803);
        final Bin bin = getBin();
        given(binRepository.findById(anyLong())).willReturn(Optional.of(bin));

        // when, then
        assertThatThrownBy(
            () -> reportService.saveClosure(1L, 1L, request))
            .isInstanceOf(InvalidValueException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.REPORT_DISTANCE_FAR);
      }
    }

    @Nested
    @DisplayName("이미 같은 폐수거함을 폐쇄 제보한 상태에서 폐쇄 제보를 한다면")
    class Context_with_already_exist_closure {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final ClosureCreateRequest request = new ClosureCreateRequest(37.583297, 126.987755);
        final Bin bin = getBin();
        final Closure closure = getClosure(bin);
        given(binRepository.findById(anyLong())).willReturn(Optional.of(bin));
        given(reportRepository.findClosureByUserIdAndBin(anyLong(), any(Bin.class))).willReturn(Optional.of(closure));

        // when, then
        assertThatThrownBy(
            () -> reportService.saveClosure(1L, 1L, request))
            .isInstanceOf(InvalidValueException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.CLOSURE_DUPLICATED);
      }
    }

    @Nested
    @DisplayName("유효한 값이 넘어오면")
    class Context_with_valid_argument {

      @Test
      @DisplayName("폐쇄 제보를 저장한다.")
      void success() throws Exception {
        //given
        final ClosureCreateRequest request = new ClosureCreateRequest(37.583297, 126.987755);
        final Bin bin = getBin();
        final Closure closure = getClosure(bin);
        given(binRepository.findById(anyLong())).willReturn(Optional.of(bin));
        given(reportRepository.findClosureByUserIdAndBin(anyLong(), any(Bin.class))).willReturn(Optional.empty());
        given(reportRepository.save(any(Closure.class))).willReturn(closure);

        // when
        final ClosureCreateResponse response = reportService.saveClosure(1L, 1L, request);

        //then
        verify(reportRepository).save(any(Closure.class));
        assertThat(response.getId()).isEqualTo(closure.getId());
      }
    }
  }
}
