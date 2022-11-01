package com.huemap.backend.report.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.report.application.ReportService;
import com.huemap.backend.report.dto.request.ClosureCreateRequest;
import com.huemap.backend.report.dto.response.ClosureCreateResponse;

@WebMvcTest(ReportController.class)
@MockBean(JpaMetamodelMappingContext.class)
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
    @DisplayName("위도가 null로 들어오면")
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
    @DisplayName("위도가 -90 ~ 90 이외의 값이거나 올바르지 않은 형식으로 들어오면")
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
    @DisplayName("경도가 null로 들어오면")
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
    @DisplayName("경도가 -180 ~ 180 이외의 값이거나 올바르지 않은 형식으로 들어오면")
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
    @DisplayName("위도와 경도가 올바른 값으로 입력되면")
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

  private ResultActions requestPost(final String url, final Object request) throws Exception {
    final String content = objectMapper.writeValueAsString(request);

    return mockMvc.perform(post(url)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                  .andDo(print());
  }
}
