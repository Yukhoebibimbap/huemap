package com.huemap.backend.domain.report.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.domain.report.application.ReportService;
import com.huemap.backend.domain.report.dto.request.ClosureCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceVoteRequest;
import com.huemap.backend.domain.report.dto.response.ClosureCreateResponse;
import com.huemap.backend.domain.report.dto.response.PresenceCreateResponse;
import com.huemap.backend.domain.report.presentation.ReportController;

@WebMvcTest(ReportController.class)
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("ReportController의")
public class ReportControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ReportService reportService;

  @Nested
  @DisplayName("saveClosure 메소드는")
  class saveClosure {

    @Nested
    @DisplayName("제보자의 위도가 null로 들어오면")
    class Context_with_null_latitude {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final ClosureCreateRequest request = new ClosureCreateRequest(null, 126.9876779);

        //when
        final ResultActions perform = requestSaveClosure(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("제보자의 위도가 -90 ~ 90 이외의 값이거나 올바르지 않은 형식으로 들어오면")
    class Context_with_invalid_range_latitude {

      @ParameterizedTest
      @ValueSource(doubles = {90.1, -91, Double.MAX_VALUE})
      @DisplayName("예외를 던진다.")
      void It_throws_exception(Double latitude) throws Exception {
        //given
        final Long binId = 1L;
        final ClosureCreateRequest request = new ClosureCreateRequest(latitude, 126.9876779);

        //when
        final ResultActions perform = requestSaveClosure(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("제보자의 경도가 null로 들어오면")
    class Context_with_null_longitude {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final ClosureCreateRequest request = new ClosureCreateRequest(37.5833354, null);

        //when
        final ResultActions perform = requestSaveClosure(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("제보자의 경도가 -180 ~ 180 이외의 값이거나 올바르지 않은 형식으로 들어오면")
    class Context_with_invalid_range_longitude {

      @ParameterizedTest
      @ValueSource(doubles = {180.1, -181, Double.MAX_VALUE})
      @DisplayName("예외를 던진다.")
      void It_throws_exception(Double longitude) throws Exception {
        //given
        final Long binId = 1L;
        final ClosureCreateRequest request = new ClosureCreateRequest(37.5833354, longitude);

        //when
        final ResultActions perform = requestSaveClosure(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("존재하지 않은 폐수거함으로 폐쇄 제보를 한다면")
    class Context_with_not_found_bin {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final ClosureCreateRequest request = new ClosureCreateRequest(37.5833354, 126.9876779);
        willThrow(new EntityNotFoundException(ErrorCode.BIN_NOT_FOUND))
            .given(reportService)
            .saveClosure(anyLong(), anyLong(), any(ClosureCreateRequest.class));

        //when
        final ResultActions perform = requestSaveClosure(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("폐수거함의 거리 차이가 10m 이상인 곳에서 폐쇄 제보를 한다면")
    class Context_with_distance_far_bin {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final ClosureCreateRequest request = new ClosureCreateRequest(37.583289, 126.987803);
        willThrow(new InvalidValueException(ErrorCode.DISTANCE_FAR))
            .given(reportService)
            .saveClosure(anyLong(), anyLong(), any(ClosureCreateRequest.class));

        //when
        final ResultActions perform = requestSaveClosure(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("같은 폐수거함에 대하여 폐쇄 제보를 한다면")
    class Context_with_already_exist_closure {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final ClosureCreateRequest request = new ClosureCreateRequest(37.583289, 126.987803);
        willThrow(new InvalidValueException(ErrorCode.CLOSURE_DUPLICATED))
            .given(reportService)
            .saveClosure(anyLong(), anyLong(), any(ClosureCreateRequest.class));

        //when
        final ResultActions perform = requestSaveClosure(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("제보자의 위도와 경도가 올바른 값으로 입력되면")
    class Context_with_valid_latitude_longitude {

      @Test
      @DisplayName("201을 응답한다.")
      void It_responses_201() throws Exception {
        //given
        final Long binId = 1L;
        final ClosureCreateRequest request = new ClosureCreateRequest(37.5833354, 126.9876779);
        final ClosureCreateResponse response = new ClosureCreateResponse(1L);
        final RestResponse restResponse = RestResponse.of(response);
        given(reportService.saveClosure(anyLong(), anyLong(), any(ClosureCreateRequest.class))).willReturn(response);

        //when
        final ResultActions perform = requestSaveClosure(binId, request);

        //then
        perform.andExpect(status().isCreated())
               .andExpect(content().json(objectMapper.writeValueAsString(restResponse)));
      }
    }

    private ResultActions requestSaveClosure(final Long binId, final ClosureCreateRequest request)
        throws Exception {
      return requestPost("/api/v1/bins/" + binId + "/report-closures", request);
    }
  }

  @Nested
  @DisplayName("savePresence 메소드는")
  class savePresence {

    @Nested
    @DisplayName("폐수거함 유형이 null로 들어오면")
    class Context_with_null_type {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final PresenceCreateRequest request = new PresenceCreateRequest(null,
                                                                        37.5833354,
                                                                        126.9876779);

        //when
        final ResultActions perform = requestSavePresence(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("유효하지 않은 폐수거함 유형이 들어오면")
    class Context_with_invalid_type {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("type", "INVALID");
        requestMap.put("latitude", 37.5833354);
        requestMap.put("longitude", 126.9876779);
        final String request = objectMapper.writeValueAsString(requestMap);

        //when
        final ResultActions perform = mockMvc.perform(post("/api/v1/bins/report-presences")
                                                          .contentType(MediaType.APPLICATION_JSON)
                                                          .content(request))
                                             .andDo(print());

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("핀의 위도가 null로 들어오면")
    class Context_with_null_latitude {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final PresenceCreateRequest request = new PresenceCreateRequest(BinType.GENERAL,
                                                                        null,
                                                                        126.9876779);

        //when
        final ResultActions perform = requestSavePresence(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("핀의  위도가 -90 ~ 90 이외의 값이거나 올바르지 않은 형식으로 들어오면")
    class Context_with_invalid_range_latitude {

      @ParameterizedTest
      @ValueSource(doubles = {90.1, -91, Double.MAX_VALUE})
      @DisplayName("예외를 던진다.")
      void It_throws_exception(Double latitude) throws Exception {
        //given
        final PresenceCreateRequest request = new PresenceCreateRequest(BinType.GENERAL,
                                                                        latitude,
                                                                        126.9876779);
        //when
        final ResultActions perform = requestSavePresence(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("핀의  경도가 null로 들어오면")
    class Context_with_null_longitude {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final PresenceCreateRequest request = new PresenceCreateRequest(BinType.GENERAL,
                                                                        37.5833354,
                                                                        null);

        //when
        final ResultActions perform = requestSavePresence(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("핀의 경도가 -180 ~ 180 이외의 값이거나 올바르지 않은 형식으로 들어오면")
    class Context_with_invalid_range_longitude {

      @ParameterizedTest
      @ValueSource(doubles = {180.1, -181, Double.MAX_VALUE})
      @DisplayName("예외를 던진다.")
      void It_throws_exception(Double longitude) throws Exception {
        //given
        final PresenceCreateRequest request = new PresenceCreateRequest(BinType.GENERAL,
                                                                        37.5833354,
                                                                        longitude);

        //when
        final ResultActions perform = requestSavePresence(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("핀의 위도, 경도, 타입이 올바른 값으로 입력되면")
    class Context_with_valid_latitude_longitude_type {

      @Test
      @DisplayName("201을 응답한다.")
      void It_responses_201() throws Exception {
        //given
        final PresenceCreateRequest request = new PresenceCreateRequest(BinType.GENERAL,
                                                                        37.5833354,
                                                                        126.9876779);
        final PresenceCreateResponse response = new PresenceCreateResponse(1L);
        final RestResponse restResponse = RestResponse.of(response);
        given(reportService.savePresence(anyLong(), any(PresenceCreateRequest.class))).willReturn(response);

        //when
        final ResultActions perform = requestSavePresence(request);

        //then
        perform.andExpect(status().isCreated())
               .andExpect(content().json(objectMapper.writeValueAsString(restResponse)));
      }
    }

    private ResultActions requestSavePresence(final PresenceCreateRequest request)
        throws Exception {
      return requestPost("/api/v1/bins/report-presences", request);
    }
  }

  @Nested
  @DisplayName("votePresence 메소드는")
  class votePresence {

    @Nested
    @DisplayName("투표자의 위도가 null로 들어오면")
    class Context_with_null_latitude {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final PresenceVoteRequest request = new PresenceVoteRequest(null, 126.9876779);

        //when
        final ResultActions perform = requestVotePresence(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("투표자의 위도가 -90 ~ 90 이외의 값이거나 올바르지 않은 형식으로 들어오면")
    class Context_with_invalid_range_latitude {

      @ParameterizedTest
      @ValueSource(doubles = {90.1, -91, Double.MAX_VALUE})
      @DisplayName("예외를 던진다.")
      void It_throws_exception(Double latitude) throws Exception {
        //given
        final Long binId = 1L;
        final PresenceVoteRequest request = new PresenceVoteRequest(latitude, 126.9876779);

        //when
        final ResultActions perform = requestVotePresence(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("핀의  경도가 null로 들어오면")
    class Context_with_null_longitude {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final PresenceVoteRequest request = new PresenceVoteRequest(37.5833354, null);

        //when
        final ResultActions perform = requestVotePresence(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("핀의 경도가 -180 ~ 180 이외의 값이거나 올바르지 않은 형식으로 들어오면")
    class Context_with_invalid_range_longitude {

      @ParameterizedTest
      @ValueSource(doubles = {180.1, -181, Double.MAX_VALUE})
      @DisplayName("예외를 던진다.")
      void It_throws_exception(Double longitude) throws Exception {
        //given
        final Long binId = 1L;
        final PresenceVoteRequest request = new PresenceVoteRequest(37.5833354, longitude);

        //when
        final ResultActions perform = requestVotePresence(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("핀의 위도, 경도, 타입이 올바른 값으로 입력되면")
    class Context_with_valid_latitude_longitude_type {

      @Test
      @DisplayName("200을 응답한다.")
      void It_responses_200() throws Exception {
        //given
        final Long binId = 1L;
        final PresenceVoteRequest request = new PresenceVoteRequest(37.5833354, 126.9876779);
        doNothing().when(reportService).votePresence(anyLong(), any(PresenceVoteRequest.class));

        //when
        final ResultActions perform = requestVotePresence(binId, request);

        //then
        perform.andExpect(status().isOk());
      }
    }

    private ResultActions requestVotePresence(final Long binId, final PresenceVoteRequest request)
        throws Exception {
      return requestPut("/api/v1/bins/" + binId + "/vote", request);
    }
  }

  private ResultActions requestPost(final String url, final Object request) throws Exception {
    final String content = objectMapper.writeValueAsString(request);

    return mockMvc.perform(post(url)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                  .andDo(print());
  }

  private ResultActions requestPut(final String url, final Object request) throws Exception {
    final String content = objectMapper.writeValueAsString(request);

    return mockMvc.perform(put(url)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                  .andDo(print());
  }
}
