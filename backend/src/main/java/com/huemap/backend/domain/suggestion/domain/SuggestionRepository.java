package com.huemap.backend.domain.suggestion.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huemap.backend.domain.bin.domain.BinType;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

	int countByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startDatetime, LocalDateTime endDatetime);

	List<Suggestion> findAllByGuAndTypeAndCreatedAtBetween(String gu, BinType type, LocalDateTime startDate,
		LocalDateTime endDate);
}
