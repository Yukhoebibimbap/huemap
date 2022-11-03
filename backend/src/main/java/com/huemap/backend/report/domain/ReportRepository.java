package com.huemap.backend.report.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huemap.backend.bin.domain.Bin;

public interface ReportRepository<T extends Report> extends JpaRepository<T, Long> {

	Optional<Closure> findByUserIdAndBin(Long userId, Bin bin);

	@Query("select count(c) from Closure c where c.bin.id = :binId")
	int countClosureByBin(@Param("binId") Long binId);
}
