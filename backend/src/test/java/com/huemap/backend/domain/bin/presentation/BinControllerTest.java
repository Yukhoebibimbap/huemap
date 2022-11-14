package com.huemap.backend.domain.bin.presentation;

import static com.huemap.backend.support.TestUtils.getBin;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.huemap.backend.support.ControllerTest;
import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.domain.bin.dto.response.BinDetailResponse;
import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.response.error.ErrorCode;


@DisplayName("BinController의")
public class BinControllerTest extends ControllerTest {

	@Nested
	@DisplayName("findAll 메소드는")
	class findAll {

		@Nested
		@DisplayName("폐수거함 유형이 잘못됬으면")
		class Context_with_invalid_BinType {

			@Test
			@DisplayName("예외를 던진다.")
			void It_throws_exception() throws Exception {
				//given
				final String type = "Clo";

				//when
				final ResultActions perform = requestFindAll(type);

				//then
				perform.andExpect(status().isBadRequest());
			}
		}

		@Nested
		@DisplayName("폐수거함 유형이 올바르면")
		class Context_with_valid_BinType {

			@Test
			@DisplayName("200을 응답한다.")
			void It_responses_200() throws Exception {
				//given
				final String type = "GENERAL";

				//when
				final ResultActions perform = requestFindAll(type);

				//then
				perform.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.data").isArray());
			}
		}

		private ResultActions requestFindAll(final String type) throws Exception {
			return requestGet("/api/v1/bins?type=" + type);
		}
	}

	@Nested
	@DisplayName("findById 메소드는")
	class findById {

		@Nested
		@DisplayName("폐수거함 id가 올바르면")
		class Context_with_valid_id {

			@Test
			@DisplayName("200을 응답한다.")
			void It_responses_200() throws Exception {
				//given
				final Long id = 1L;
				final Bin bin = getBin();
				given(binService.findById(id)).willReturn(BinDetailResponse.toDto(bin,anyBoolean()));

				//when
				final ResultActions perform = requestFindById(id);

				//then
				perform.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.data.address").value("서울특별시 종로구 창덕궁7길 5"));
			}
		}

		@Nested
		@DisplayName("존재하지 않는 폐수거함 id가 넘어오면")
		class Context_with_invalid_id {

			@Test
			@DisplayName("예외를 던진다.")
			void It_throws_exception() throws Exception {
				//given
				final Long id = 2L;
				given(binService.findById(id)).willThrow(new EntityNotFoundException(ErrorCode.BIN_NOT_FOUND));

				//when
				final ResultActions perform = requestFindById(id);

				//then
				perform.andExpect(status().isBadRequest());
			}
		}

		private ResultActions requestFindById(final Long id) throws Exception {
			return requestGet("/api/v1/bins/" + id);
		}
	}

	private ResultActions requestGet(final String url) throws Exception {
		return mockMvc.perform(get(url)).andDo(print());
	}
}
