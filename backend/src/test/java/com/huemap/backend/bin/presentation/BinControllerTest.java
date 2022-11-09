package com.huemap.backend.bin.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.huemap.backend.bin.application.BinService;

@WebMvcTest(BinController.class)
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("BinController의")
public class BinControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BinService binService;

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
			void It_throws_exception() throws Exception {
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

	private ResultActions requestGet(final String url) throws Exception {
		return mockMvc.perform(get(url)).andDo(print());
	}
}
