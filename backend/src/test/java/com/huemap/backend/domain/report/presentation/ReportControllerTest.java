package com.huemap.backend.domain.report.presentation;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.bin.domain.ConditionType;
import com.huemap.backend.domain.report.dto.request.ClosureCreateRequest;
import com.huemap.backend.domain.report.dto.request.ConditionCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceCreateRequest;
import com.huemap.backend.domain.report.dto.request.PresenceVoteRequest;
import com.huemap.backend.domain.report.dto.response.ClosureCreateResponse;
import com.huemap.backend.domain.report.dto.response.ConditionCreateResponse;
import com.huemap.backend.domain.report.dto.response.PresenceCreateResponse;
import com.huemap.backend.support.ControllerTest;
import com.huemap.backend.support.WithMockCustomUser;

@DisplayName("ReportController???")
public class ReportControllerTest extends ControllerTest {

  @WithMockCustomUser
  @Nested
  @DisplayName("saveClosure ????????????")
  class saveClosure {

    @Nested
    @DisplayName("???????????? ????????? null??? ????????????")
    class Context_with_null_latitude {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("???????????? ????????? -90 ~ 90 ????????? ???????????? ???????????? ?????? ???????????? ????????????")
    class Context_with_invalid_range_latitude {

      @ParameterizedTest
      @ValueSource(doubles = {90.1, -91, Double.MAX_VALUE})
      @DisplayName("????????? ?????????.")
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
    @DisplayName("???????????? ????????? null??? ????????????")
    class Context_with_null_longitude {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("???????????? ????????? -180 ~ 180 ????????? ???????????? ???????????? ?????? ???????????? ????????????")
    class Context_with_invalid_range_longitude {

      @ParameterizedTest
      @ValueSource(doubles = {180.1, -181, Double.MAX_VALUE})
      @DisplayName("????????? ?????????.")
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
    @DisplayName("???????????? ?????? ?????????????????? ?????? ????????? ?????????")
    class Context_with_not_found_bin {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("??????????????? ?????? ????????? 10m ????????? ????????? ?????? ????????? ?????????")
    class Context_with_distance_far_bin {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("?????? ??????????????? ????????? ?????? ????????? ?????????")
    class Context_with_already_exist_closure {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("???????????? ????????? ????????? ????????? ????????? ????????????")
    class Context_with_valid_latitude_longitude {

      @Test
      @DisplayName("201??? ????????????.")
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

  @WithMockCustomUser
  @Nested
  @DisplayName("savePresence ????????????")
  class savePresence {

    @Nested
    @DisplayName("???????????? ????????? null??? ????????????")
    class Context_with_null_type {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("???????????? ?????? ???????????? ????????? ????????????")
    class Context_with_invalid_type {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("type", "INVALID");
        requestMap.put("latitude", 37.5833354);
        requestMap.put("longitude", 126.9876779);
        final String request = objectMapper.writeValueAsString(requestMap);

        //when
        final ResultActions perform = mockMvc.perform(post("/api/v1/bins/report-presences")
                                                          .with(csrf())
                                                          .contentType(MediaType.APPLICATION_JSON)
                                                          .content(request))
                                             .andDo(print());

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("?????? ????????? null??? ????????????")
    class Context_with_null_latitude {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("?????? ????????? -90 ~ 90 ????????? ???????????? ???????????? ?????? ???????????? ????????????")
    class Context_with_invalid_range_latitude {

      @ParameterizedTest
      @ValueSource(doubles = {90.1, -91, Double.MAX_VALUE})
      @DisplayName("????????? ?????????.")
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
    @DisplayName("?????? ????????? null??? ????????????")
    class Context_with_null_longitude {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("?????? ????????? -180 ~ 180 ????????? ???????????? ???????????? ?????? ???????????? ????????????")
    class Context_with_invalid_range_longitude {

      @ParameterizedTest
      @ValueSource(doubles = {180.1, -181, Double.MAX_VALUE})
      @DisplayName("????????? ?????????.")
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
    @DisplayName("?????? ??????, ??????, ????????? ????????? ????????? ????????????")
    class Context_with_valid_latitude_longitude_type {

      @Test
      @DisplayName("201??? ????????????.")
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

  @WithMockCustomUser
  @Nested
  @DisplayName("votePresence ????????????")
  class votePresence {

    @Nested
    @DisplayName("???????????? ????????? null??? ????????????")
    class Context_with_null_latitude {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("???????????? ????????? -90 ~ 90 ????????? ???????????? ???????????? ?????? ???????????? ????????????")
    class Context_with_invalid_range_latitude {

      @ParameterizedTest
      @ValueSource(doubles = {90.1, -91, Double.MAX_VALUE})
      @DisplayName("????????? ?????????.")
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
    @DisplayName("???????????? ????????? null??? ????????????")
    class Context_with_null_longitude {

      @Test
      @DisplayName("????????? ?????????.")
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
    @DisplayName("???????????? ????????? -180 ~ 180 ????????? ???????????? ???????????? ?????? ???????????? ????????????")
    class Context_with_invalid_range_longitude {

      @ParameterizedTest
      @ValueSource(doubles = {180.1, -181, Double.MAX_VALUE})
      @DisplayName("????????? ?????????.")
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
    @DisplayName("???????????? ?????? ?????????????????? ????????? ?????????")
    class Context_with_not_found_bin {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final PresenceVoteRequest request = new PresenceVoteRequest(37.5833354, 126.9876779);
        willThrow(new EntityNotFoundException(ErrorCode.BIN_NOT_FOUND))
            .given(reportService)
            .votePresence(anyLong(), any(PresenceVoteRequest.class));

        //when
        final ResultActions perform = requestVotePresence(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("??????????????? ??????????????? ?????? ????????? 10m ????????? ????????? ????????? ?????????")
    class Context_with_distance_far_bin {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final PresenceVoteRequest request = new PresenceVoteRequest(37.5833354, 126.9876779);
        willThrow(new InvalidValueException(ErrorCode.DISTANCE_FAR))
            .given(reportService)
            .votePresence(anyLong(), any(PresenceVoteRequest.class));

        //when
        final ResultActions perform = requestVotePresence(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("??????????????? ??????????????? ?????? ???????????? ?????? ??????????????????")
    class Context_with_not_exist_presence {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final PresenceVoteRequest request = new PresenceVoteRequest(37.5833354, 126.9876779);
        willThrow(new EntityNotFoundException(ErrorCode.PRESENCE_NOT_FOUND))
            .given(reportService)
            .votePresence(anyLong(), any(PresenceVoteRequest.class));

        //when
        final ResultActions perform = requestVotePresence(binId, request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("???????????? ??????, ????????? ????????? ????????? ????????????")
    class Context_with_valid_latitude_longitude {

      @Test
      @DisplayName("200??? ????????????.")
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

  @WithMockCustomUser
  @Nested
  @DisplayName("saveCondition ????????????")
  class saveCondition {

    @Nested
    @DisplayName("???????????? ?????? ????????? null??? ????????????")
    class Context_with_null_type {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final ConditionCreateRequest request = new ConditionCreateRequest(null,
            37.5833354,
            126.9876779);
        final String content = objectMapper.writeValueAsString(request);
        final MockMultipartFile dto = new MockMultipartFile("dto",
            "",
            "application/json",
            content.getBytes(StandardCharsets.UTF_8));
        final MockMultipartFile file = new MockMultipartFile("file",
            "imagefile.png",
            "image/png",
            "<<png data>>".getBytes());

        //when
        final ResultActions perform = requestSaveCondition(binId, dto, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("???????????? ?????? ???????????? ?????? ????????? ????????????")
    class Context_with_invalid_type {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("type", "INVALID");
        requestMap.put("latitude", 37.5833354);
        requestMap.put("longitude", 126.9876779);
        final String content = objectMapper.writeValueAsString(requestMap);
        final MockMultipartFile dto = new MockMultipartFile("dto",
            "",
            "application/json",
            content.getBytes(StandardCharsets.UTF_8));
        final MockMultipartFile file = new MockMultipartFile("file",
            "imagefile.png",
            "image/png",
            "<<png data>>".getBytes());

        //when
        final ResultActions perform = mockMvc.perform(multipart("/api/v1/bins/" + binId + "/report-condition")
                .file(dto)
                .file(file)
                .contentType("multipart/form-data")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .with(csrf()))
            .andDo(print());

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("?????? ???????????? ????????? null??? ????????????")
    class Context_with_null_latitude {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final ConditionCreateRequest request = new ConditionCreateRequest(ConditionType.FULL,
            null,
            126.9876779);
        final String content = objectMapper.writeValueAsString(request);
        final MockMultipartFile dto = new MockMultipartFile("dto",
            "",
            "application/json",
            content.getBytes(StandardCharsets.UTF_8));
        final MockMultipartFile file = new MockMultipartFile("file",
            "imagefile.png",
            "image/png",
            "<<png data>>".getBytes());

        //when
        final ResultActions perform = requestSaveCondition(binId, dto, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("?????? ???????????? ????????? -90 ~ 90 ????????? ???????????? ???????????? ?????? ???????????? ????????????")
    class Context_with_invalid_range_latitude {

      @ParameterizedTest
      @ValueSource(doubles = {90.1, -91, Double.MAX_VALUE})
      @DisplayName("????????? ?????????.")
      void It_throws_exception(Double latitude) throws Exception {
        //given
        final Long binId = 1L;
        final ConditionCreateRequest request = new ConditionCreateRequest(ConditionType.FULL,
            latitude,
            126.9876779);
        final String content = objectMapper.writeValueAsString(request);
        final MockMultipartFile dto = new MockMultipartFile("dto",
            "",
            "application/json",
            content.getBytes(StandardCharsets.UTF_8));
        final MockMultipartFile file = new MockMultipartFile("file",
            "imagefile.png",
            "image/png",
            "<<png data>>".getBytes());

        //when
        final ResultActions perform = requestSaveCondition(binId, dto, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("?????? ???????????? ????????? null??? ????????????")
    class Context_with_null_longitude {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final ConditionCreateRequest request = new ConditionCreateRequest(ConditionType.FULL,
            37.5833354,
            null);
        final String content = objectMapper.writeValueAsString(request);
        final MockMultipartFile dto = new MockMultipartFile("dto",
            "",
            "application/json",
            content.getBytes(StandardCharsets.UTF_8));
        final MockMultipartFile file = new MockMultipartFile("file",
            "imagefile.png",
            "image/png",
            "<<png data>>".getBytes());

        //when
        final ResultActions perform = requestSaveCondition(binId, dto, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("?????? ???????????? ????????? -180 ~ 180 ????????? ???????????? ???????????? ?????? ???????????? ????????????")
    class Context_with_invalid_range_longitude {

      @ParameterizedTest
      @ValueSource(doubles = {180.1, -181, Double.MAX_VALUE})
      @DisplayName("????????? ?????????.")
      void It_throws_exception(Double longitude) throws Exception {
        //given
        final Long binId = 1L;
        final ConditionCreateRequest request = new ConditionCreateRequest(ConditionType.FULL,
            37.5833354,
            longitude);
        final String content = objectMapper.writeValueAsString(request);
        final MockMultipartFile dto = new MockMultipartFile("dto",
            "",
            "application/json",
            content.getBytes(StandardCharsets.UTF_8));
        final MockMultipartFile file = new MockMultipartFile("file",
            "imagefile.png",
            "image/png",
            "<<png data>>".getBytes());

        //when
        final ResultActions perform = requestSaveCondition(binId, dto, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("???????????? ?????? ?????????????????? ?????? ????????? ?????????")
    class Context_with_not_found_bin {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final ConditionCreateRequest request = new ConditionCreateRequest(ConditionType.FULL,
            37.5833354,
            126.9876779);
        final String content = objectMapper.writeValueAsString(request);
        final MockMultipartFile dto = new MockMultipartFile("dto",
            "",
            "application/json",
            content.getBytes(StandardCharsets.UTF_8));
        final MockMultipartFile file = new MockMultipartFile("file",
            "imagefile.png",
            "image/png",
            "<<png data>>".getBytes());
        willThrow(new EntityNotFoundException(ErrorCode.BIN_NOT_FOUND))
            .given(reportService)
            .saveCondition(anyLong(), anyLong(), any(ConditionCreateRequest.class),
                any(MultipartFile.class));

        //when
        final ResultActions perform = requestSaveCondition(binId, dto, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("??????????????? ??????????????? ?????? ????????? 10m ????????? ????????? ?????? ????????? ?????????")
    class Context_with_distance_far_bin {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final ConditionCreateRequest request = new ConditionCreateRequest(ConditionType.FULL,
            37.5833354,
            126.9876779);
        final String content = objectMapper.writeValueAsString(request);
        final MockMultipartFile dto = new MockMultipartFile("dto",
            "",
            "application/json",
            content.getBytes(StandardCharsets.UTF_8));
        final MockMultipartFile file = new MockMultipartFile("file",
            "imagefile.png",
            "image/png",
            "<<png data>>".getBytes());
        willThrow(new InvalidValueException(ErrorCode.DISTANCE_FAR))
            .given(reportService)
            .saveCondition(anyLong(), anyLong(), any(ConditionCreateRequest.class),
                any(MultipartFile.class));

        //when
        final ResultActions perform = requestSaveCondition(binId, dto, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("???????????? ??????, ??????, ??????, ????????? ????????? ????????? ????????? ????????????")
    class Context_with_valid_latitude_longitude_type_file {

      @Test
      @DisplayName("201??? ????????????.")
      void It_responses_201() throws Exception {
        //given
        final Long binId = 1L;
        final ConditionCreateRequest request = new ConditionCreateRequest(ConditionType.FULL,
            37.5833354,
            126.9876779);
        final String content = objectMapper.writeValueAsString(request);
        final MockMultipartFile dto = new MockMultipartFile("dto",
            "",
            "application/json",
            content.getBytes(StandardCharsets.UTF_8));
        final MockMultipartFile file = new MockMultipartFile("file",
            "imagefile.png",
            "image/png",
            "<<png data>>".getBytes());
        final ConditionCreateResponse response = new ConditionCreateResponse(1L);
        final RestResponse restResponse = RestResponse.of(response);
        given(reportService.saveCondition(anyLong(), anyLong(), any(ConditionCreateRequest.class), any(
            MultipartFile.class))).willReturn(response);

        //when
        final ResultActions perform = requestSaveCondition(binId, dto, file);

        //then
        perform.andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(restResponse)));
      }
    }

    private ResultActions requestSaveCondition(final Long binId, final MockMultipartFile dto, final MockMultipartFile file)
        throws Exception {
      return requestPostWithMultiPart("/api/v1/bins/" + binId + "/report-condition", dto, file);
    }
  }

  @WithMockCustomUser
  @Nested
  @DisplayName("saveCondition ????????????")
  class saveConditionV2 {

    @Nested
    @DisplayName("???????????? ?????? ????????? null??? ????????????")
    class Context_with_null_type {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final String type = null;
        final String latitude = "37.5833354";
        final String longitude = "126.9876779";

        final MockMultipartFile file = new MockMultipartFile("file",
                                                              "imagefile.png",
                                                              "image/png",
                                                              "<<png data>>".getBytes());

        //when
        final ResultActions perform = requestSaveCondition(binId, type, latitude, longitude, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("???????????? ?????? ???????????? ?????? ????????? ????????????")
    class Context_with_invalid_type {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final String type = "FULL2";
        final String latitude = "37.5833354";
        final String longitude = "126.9876779";

        final MockMultipartFile file = new MockMultipartFile("file",
                                                             "imagefile.png",
                                                             "image/png",
                                                             "<<png data>>".getBytes());

        //when
        final ResultActions perform = requestSaveCondition(binId, type, latitude, longitude, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("?????? ???????????? ????????? null??? ????????????")
    class Context_with_null_latitude {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final String type = "FULL";
        final String latitude = null;
        final String longitude = "126.9876779";

        final MockMultipartFile file = new MockMultipartFile("file",
                                                             "imagefile.png",
                                                             "image/png",
                                                             "<<png data>>".getBytes());

        //when
        final ResultActions perform = requestSaveCondition(binId, type, latitude, longitude, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("?????? ???????????? ????????? -90 ~ 90 ????????? ???????????? ???????????? ?????? ???????????? ????????????")
    class Context_with_invalid_range_latitude {

      @ParameterizedTest
      @ValueSource(doubles = {90.1, -91, Double.MAX_VALUE})
      @DisplayName("????????? ?????????.")
      void It_throws_exception(Double latitude) throws Exception {
        //given
        final Long binId = 1L;
        final String type = "FULL";
        final String longitude = "126.9876779";

        final MockMultipartFile file = new MockMultipartFile("file",
                                                             "imagefile.png",
                                                             "image/png",
                                                             "<<png data>>".getBytes());

        //when
        final ResultActions perform = requestSaveCondition(binId, type, String.valueOf(latitude), longitude, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("?????? ???????????? ????????? null??? ????????????")
    class Context_with_null_longitude {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final String type = "FULL";
        final String latitude = "37.5833354";
        final String longitude = null;

        final MockMultipartFile file = new MockMultipartFile("file",
                                                             "imagefile.png",
                                                             "image/png",
                                                             "<<png data>>".getBytes());

        //when
        final ResultActions perform = requestSaveCondition(binId, type, latitude, longitude, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("?????? ???????????? ????????? -180 ~ 180 ????????? ???????????? ???????????? ?????? ???????????? ????????????")
    class Context_with_invalid_range_longitude {

      @ParameterizedTest
      @ValueSource(doubles = {180.1, -181, Double.MAX_VALUE})
      @DisplayName("????????? ?????????.")
      void It_throws_exception(Double longitude) throws Exception {
        //given
        final Long binId = 1L;
        final String type = "FULL";
        final String latitude = "37.5833354";

        final MockMultipartFile file = new MockMultipartFile("file",
                                                             "imagefile.png",
                                                             "image/png",
                                                             "<<png data>>".getBytes());

        //when
        final ResultActions perform = requestSaveCondition(binId, type, latitude, String.valueOf(longitude), file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("???????????? ?????? ?????????????????? ?????? ????????? ?????????")
    class Context_with_not_found_bin {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final String type = "FULL";
        final String latitude = "37.5833354";
        final String longitude = "126.9876779";

        final MockMultipartFile file = new MockMultipartFile("file",
                                                             "imagefile.png",
                                                             "image/png",
                                                             "<<png data>>".getBytes());
        willThrow(new EntityNotFoundException(ErrorCode.BIN_NOT_FOUND))
            .given(reportService)
            .saveCondition(anyLong(), anyLong(), any(ConditionCreateRequest.class),
                           any(MultipartFile.class));

        //when
        final ResultActions perform = requestSaveCondition(binId, type, latitude, longitude, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("??????????????? ??????????????? ?????? ????????? 10m ????????? ????????? ?????? ????????? ?????????")
    class Context_with_distance_far_bin {

      @Test
      @DisplayName("????????? ?????????.")
      void It_throws_exception() throws Exception {
        //given
        final Long binId = 1L;
        final String type = "FULL";
        final String latitude = "37.5833354";
        final String longitude = "126.9876779";

        final MockMultipartFile file = new MockMultipartFile("file",
                                                             "imagefile.png",
                                                             "image/png",
                                                             "<<png data>>".getBytes());
        willThrow(new InvalidValueException(ErrorCode.DISTANCE_FAR))
            .given(reportService)
            .saveCondition(anyLong(), anyLong(), any(ConditionCreateRequest.class),
                           any(MultipartFile.class));

        //when
        final ResultActions perform = requestSaveCondition(binId, type, latitude, longitude, file);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("???????????? ??????, ??????, ??????, ????????? ????????? ????????? ????????? ????????????")
    class Context_with_valid_latitude_longitude_type_file {

      @Test
      @DisplayName("201??? ????????????.")
      void It_responses_201() throws Exception {
        //given
        final Long binId = 1L;
        final String type = "FULL";
        final String latitude = "37.5833354";
        final String longitude = "126.9876779";


        final MockMultipartFile file = new MockMultipartFile("file",
                                                             "imagefile.png",
                                                             "image/png",
                                                             "<<png data>>".getBytes());
        final ConditionCreateResponse response = new ConditionCreateResponse(1L);
        final RestResponse restResponse = RestResponse.of(response);
        given(reportService.saveCondition(anyLong(), anyLong(), any(ConditionCreateRequest.class), any(
            MultipartFile.class))).willReturn(response);

        //when
        final ResultActions perform = requestSaveCondition(binId, type, latitude, longitude, file);

        //then
        perform.andExpect(status().isCreated())
               .andExpect(content().json(objectMapper.writeValueAsString(restResponse)));
      }
    }

    private ResultActions requestSaveCondition(final Long binId, String type, String latitude, String longitude,
        final MockMultipartFile file)
        throws Exception {
      return requestPostWithMultiPart("/api/v2/bins/" + binId + "/report-condition?type=" + type +
          "&latitude=" + latitude + "&longitude=" + longitude, file);
    }
  }

  private ResultActions requestPostWithMultiPart(final String url, final MockMultipartFile file) throws Exception {
    return mockMvc.perform(multipart(url)
                               .file(file)
                               .contentType("multipart/form-data")
                               .accept(MediaType.APPLICATION_JSON)
                               .characterEncoding("UTF-8")
                               .with(csrf()))
                  .andDo(print());
  }

  private ResultActions requestPostWithMultiPart(final String url, final MockMultipartFile dto, final MockMultipartFile file) throws
      Exception {
    return mockMvc.perform(multipart(url)
            .file(dto)
            .file(file)
            .contentType("multipart/form-data")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .with(csrf()))
        .andDo(print());
  }
}
