package com.huemap.backend.domain.report.application;

import static com.huemap.backend.support.TestUtils.getBin;
import static com.huemap.backend.support.TestUtils.getClosure;
import static com.huemap.backend.support.TestUtils.getCondition;
import static com.huemap.backend.support.TestUtils.getPresence;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;

import com.corundumstudio.socketio.SocketIOServer;
import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.domain.bin.domain.BinRepository;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.bin.domain.ConditionType;
import com.huemap.backend.domain.report.domain.Closure;
import com.huemap.backend.domain.report.domain.Condition;
import com.huemap.backend.domain.report.domain.Image;
import com.huemap.backend.domain.report.domain.Presence;
import com.huemap.backend.domain.report.domain.ReportRepository;
import com.huemap.backend.domain.report.dto.request.ClosureCreateRequest;
import com.huemap.backend.domain.report.dto.request.ConditionCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceVoteRequest;
import com.huemap.backend.domain.report.dto.response.ClosureCreateResponse;
import com.huemap.backend.domain.report.dto.response.ConditionCreateResponse;
import com.huemap.backend.domain.report.dto.response.PresenceCreateResponse;
import com.huemap.backend.infrastructure.s3.S3Uploader;
import com.huemap.backend.infrastructure.socket.SocketService;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReportService???")
public class ReportServiceTest {

  @InjectMocks
  private ReportService reportService;

  @Mock
  private ReportRepository reportRepository;

  @Mock
  private BinRepository binRepository;

  @Mock
  private ApplicationEventPublisher publisher;

  @Mock
  private S3Uploader s3Uploader;

  @Mock
  private SocketService socketService;

  @Mock
  private SocketIOServer socketIOServer;

  @Nested
  @DisplayName("saveClosure ????????????")
  class saveClosure {

    @Nested
    @DisplayName("??????????????? ???????????? ?????? ??????")
    class Context_with_not_found_bin {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("?????? ??????????????? ????????? ??????????????? ?????? ????????? 10m ????????????")
    class Context_with_distance_far_bin {

      @Test
      @DisplayName("????????? ?????????.")
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
            .isEqualTo(ErrorCode.DISTANCE_FAR);
      }
    }

    @Nested
    @DisplayName("?????? ?????? ??????????????? ?????? ????????? ???????????? ?????? ????????? ?????????")
    class Context_with_already_exist_closure {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("????????? ?????? ????????????")
    class Context_with_valid_argument {

      @Test
      @DisplayName("?????? ????????? ????????????.")
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

  @Nested
  @DisplayName("savePresence ????????????")
  class savePresence {

    @Nested
    @DisplayName("????????? ?????? ????????????")
    class Context_with_valid_argument {

      @Test
      @DisplayName("?????? ????????? ????????????.")
      void success() throws Exception {
        //given
        final PresenceCreateRequest request = new PresenceCreateRequest(BinType.CLOTHES,
                                                                        37.583297,
                                                                        126.987755);
        final Bin bin = getBin();
        final Presence presence = getPresence(bin);
        given(binRepository.findBinByTypeAndLocation(any(BinType.class), any(Point.class)))
            .willReturn(Optional.of(bin));
        given(reportRepository.save(any(Presence.class))).willReturn(presence);

        // when
        final PresenceCreateResponse response = reportService.savePresence(1L, request);

        //then
        verify(reportRepository).save(any(Presence.class));
        assertThat(response.getId()).isEqualTo(presence.getId());
      }
    }
  }

  @Nested
  @DisplayName("votePresence ????????????")
  class votePresence {

    @Nested
    @DisplayName("?????? ??????????????? ???????????? ?????? ??????")
    class Context_with_not_found_bin {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() {
        //given
        final PresenceVoteRequest request = new PresenceVoteRequest(37.583297, 126.987755);
        given(binRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(
            () -> reportService.votePresence( 1L, request))
            .isInstanceOf(EntityNotFoundException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.BIN_NOT_FOUND);
      }
    }

    @Nested
    @DisplayName("???????????? ????????? ??????????????? ?????? ??????????????? ?????? ????????? 10m ????????????")
    class Context_with_distance_far_bin {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final PresenceVoteRequest request = new PresenceVoteRequest(37.583289, 126.987803);
        final Bin bin = getBin();
        given(binRepository.findById(anyLong())).willReturn(Optional.of(bin));

        // when, then
        assertThatThrownBy(
            () -> reportService.votePresence(1L, request))
            .isInstanceOf(InvalidValueException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.DISTANCE_FAR);
      }
    }

    @Nested
    @DisplayName("??????????????? ?????? ???????????? ????????? ???????????? ?????????")
    class Context_not_found_presence {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final PresenceVoteRequest request = new PresenceVoteRequest(37.583297, 126.987755);
        final Bin bin = getBin();
        given(binRepository.findById(anyLong())).willReturn(Optional.of(bin));
        given(reportRepository.findPresenceByBinAndDeletedFalse(any(Bin.class))).willReturn(
            Optional.empty());

        // when, then
        assertThatThrownBy(
            () -> reportService.votePresence( 1L, request))
            .isInstanceOf(EntityNotFoundException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.PRESENCE_NOT_FOUND);
      }
    }

    @Nested
    @DisplayName("????????? ?????? ????????????")
    class Context_with_valid_argument {

      @Test
      @DisplayName("?????? ????????? ????????????.")
      void success() throws Exception {
        //given
        final PresenceVoteRequest request = new PresenceVoteRequest(37.583297, 126.987755);
        final Bin bin = getBin();
        final Presence presence = getPresence(bin);
        final int expectedCount = presence.getCount() + 1;
        given(binRepository.findById(anyLong())).willReturn(Optional.of(bin));
        given(reportRepository.findPresenceByBinAndDeletedFalse(any(Bin.class))).willReturn(
            Optional.of(presence));

        // when
        reportService.votePresence(1L, request);

        //then
        assertThat(presence.getCount()).isEqualTo(expectedCount);
      }
    }
  }

  @Nested
  @DisplayName("saveCondition ????????????")
  class saveCondition {

    @Nested
    @DisplayName("??????????????? ???????????? ?????? ??????")
    class Context_with_not_found_bin {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() {
        //given
        final ConditionCreateRequest request = new ConditionCreateRequest(ConditionType.FULL,
                                                                          37.583297,
                                                                          126.987755);
        final MockMultipartFile multipartFile = new MockMultipartFile("file",
                                                             "imagefile.png",
                                                             "image/png",
                                                             "<<png data>>".getBytes());
        given(binRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(
            () -> reportService.saveCondition( 1L, 1L, request, multipartFile))
            .isInstanceOf(EntityNotFoundException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.BIN_NOT_FOUND);
      }
    }

    @Nested
    @DisplayName("???????????? ????????? ??????????????? ??????????????? ?????? ????????? 10m ????????????")
    class Context_with_distance_far_bin {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final ConditionCreateRequest request = new ConditionCreateRequest(ConditionType.FULL,
                                                                          37.583289,
                                                                          126.987803);
        final MockMultipartFile multipartFile = new MockMultipartFile("file",
                                                                      "imagefile.png",
                                                                      "image/png",
                                                                      "<<png data>>".getBytes());
        final Bin bin = getBin();
        given(binRepository.findById(anyLong())).willReturn(Optional.of(bin));

        // when, then
        assertThatThrownBy(
            () -> reportService.saveCondition( 1L, 1L, request, multipartFile))
            .isInstanceOf(InvalidValueException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.DISTANCE_FAR);
      }
    }

    @Nested
    @DisplayName("????????? ?????? ????????????")
    class Context_with_valid_argument {

      @Test
      @DisplayName("?????? ????????? ????????????.")
      void success() throws Exception {
        //given
        final ConditionCreateRequest request = new ConditionCreateRequest(ConditionType.FULL,
                                                                          37.583297,
                                                                          126.987755);
        final MockMultipartFile multipartFile = new MockMultipartFile("file",
                                                                      "imagefile.png",
                                                                      "image/png",
                                                                      "<<png data>>".getBytes());
        final Bin bin = getBin();
        final String imgUrl = "https://huemap-s3.test.png";
        final Condition condition = getCondition(bin, new Image(imgUrl),ConditionType.FULL);
        given(binRepository.findById(anyLong())).willReturn(Optional.of(bin));
        given(s3Uploader.upload(multipartFile)).willReturn(imgUrl);
        given(reportRepository.save(any(Condition.class))).willReturn(condition);


        // when
        final ConditionCreateResponse response = reportService.saveCondition(1L,
                                                                             1L,
                                                                             request,
                                                                             multipartFile);

        //then
        verify(reportRepository).save(any(Condition.class));
        assertThat(response.getId()).isEqualTo(condition.getId());
      }
    }
  }
}
