package com.huemap.backend.domain.suggestion.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.ResultActions;

import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.suggestion.dto.request.SuggestionCreateRequest;
import com.huemap.backend.domain.suggestion.dto.response.SuggestionCreateResponse;
import com.huemap.backend.support.ControllerTest;

@DisplayName("SuggestionController의")
public class SuggestionControllerTest extends ControllerTest {

	@Nested
	@DisplayName("save 메소드는")
	class findAll {

		@Nested
		@DisplayName("제보자의 위도가 null로 들어오면")
		class Context_with_null_latitude {

			@Test
			@DisplayName("예외를 던진다.")
			void It_throws_exception() throws Exception {
				//given
				final SuggestionCreateRequest request = SuggestionCreateRequest.builder()
					.gu("강남구")
					.type(BinType.GENERAL)
					.latitude(null)
					.longitude(126.9876779)
					.build();

				//when
				final ResultActions perform = requestSave(request);

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
				final SuggestionCreateRequest request = SuggestionCreateRequest.builder()
					.gu("강남구")
					.type(BinType.GENERAL)
					.latitude(latitude)
					.longitude(126.9876779)
					.build();

				//when
				final ResultActions perform = requestSave(request);

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
				final SuggestionCreateRequest request = SuggestionCreateRequest.builder()
					.gu("강남구")
					.type(BinType.GENERAL)
					.latitude(37.5833354)
					.longitude(null)
					.build();

				//when
				final ResultActions perform = requestSave(request);

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
				final SuggestionCreateRequest request = SuggestionCreateRequest.builder()
					.gu("강남구")
					.type(BinType.GENERAL)
					.latitude(37.5833354)
					.longitude(longitude)
					.build();

				//when
				final ResultActions perform = requestSave(request);

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
				final SuggestionCreateResponse response = new SuggestionCreateResponse(1L);
				final RestResponse restResponse = RestResponse.of(response);

				final SuggestionCreateRequest request = SuggestionCreateRequest.builder()
					.gu("강남구")
					.type(BinType.GENERAL)
					.latitude(37.5833354)
					.longitude(126.9876779)
					.build();

				given(suggestionService.save(any(SuggestionCreateRequest.class), anyLong())).willReturn(response);

				//when
				final ResultActions perform = requestSave(request);

				//then
				perform.andExpect(status().isCreated())
					.andExpect(content().json(objectMapper.writeValueAsString(restResponse)));
			}
		}

		private ResultActions requestSave(final SuggestionCreateRequest request) throws Exception {
			return requestPost("/api/v1/suggestions/bin-location", request);
		}
	}
}
