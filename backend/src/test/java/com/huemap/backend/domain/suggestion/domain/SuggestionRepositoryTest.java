package com.huemap.backend.domain.suggestion.domain;

import static com.huemap.backend.support.TestUtils.getSuggestion;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.huemap.backend.support.RepositoryTest;

@DisplayName("SuggestionRepository의")
public class SuggestionRepositoryTest extends RepositoryTest {

	@Nested
	@DisplayName("countByUserIdAndCreatedAtBetween 메소드는")
	class CountByUserIdAndCreatedAtBetween {

		@Nested
		@DisplayName("userId와 startDate와 endDate를 입력받으면")
		class Context_with_userId_startDate_endDate {

			@Test
			@DisplayName("startDate와 endDate 기간 동안의 해당 사용자의 건의 횟수를 반환한다")
			void It_returns_suggestion_count() throws Exception {
				//given
				final Long userId = 1L;
				suggestionRepository.save(getSuggestion(userId));
				suggestionRepository.save(getSuggestion(userId));

				final LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
				final LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

				//when
				final int count = suggestionRepository.countByUserIdAndCreatedAtBetween(userId, startDatetime,
					endDatetime);

				//then
				assertThat(count).isEqualTo(2);
			}
		}
	}

}