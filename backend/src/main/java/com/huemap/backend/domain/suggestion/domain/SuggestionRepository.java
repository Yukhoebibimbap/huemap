package com.huemap.backend.domain.suggestion.domain;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

	int countByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startDatetime, LocalDateTime endDatetime);
}
