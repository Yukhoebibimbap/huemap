package com.huemap.backend.domain.suggestion.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.huemap.backend.common.exception.BusinessException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.suggestion.domain.SuggestionRepository;
import com.huemap.backend.domain.suggestion.dto.request.SuggestionCreateRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("SuggestionService의")
public class SuggestionServiceTest {
	@InjectMocks
	private SuggestionService suggestionService;

	@Mock
	private SuggestionRepository suggestionRepository;

	private final static int SUGGESTION_COUNT_LIMIT = 3;

	@Nested
	@DisplayName("save 메소드는")
	class save {

		@Nested
		@DisplayName("사용자의 하루 건의 초과치를 넘으면")
		class Context_with_exceed_suggestion_limit {

			@Test
			@DisplayName("예외를 던진다")
			void success() throws Exception {
				//given
				final Long userId = 1L;

				final SuggestionCreateRequest request = SuggestionCreateRequest.builder()
					.gu("강남구")
					.type(BinType.GENERAL)
					.latitude(37.5833354)
					.longitude(126.9876779)
					.build();

				given(suggestionRepository.countByUserIdAndCreatedAtBetween(anyLong(),
					any(LocalDateTime.class),
					any(LocalDateTime.class))).willReturn(SUGGESTION_COUNT_LIMIT);

				//when //then
				assertThatThrownBy(
					() -> suggestionService.save(request, userId))
					.isInstanceOf(BusinessException.class)
					.extracting("errorCode")
					.isEqualTo(ErrorCode.SUGGESTION_LIMIT_EXCEEDED);
			}
		}

	}
}
