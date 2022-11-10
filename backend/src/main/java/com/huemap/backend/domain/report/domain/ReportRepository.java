package com.huemap.backend.domain.report.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huemap.backend.domain.bin.domain.Bin;

public interface ReportRepository<T extends Report> extends JpaRepository<T, Long> {

	Optional<Closure> findClosureByUserIdAndBin(Long userId, Bin bin);

	@Query("select count(c) from Closure c where c.bin.id = :binId")
	int countClosureByBin(@Param("binId") Long binId);

	List<Presence> findPresenceByDeletedFalseAndCountGreaterThanEqual(int count);

	@Query("SELECT c "
			+ "FROM Closure c "
			+ "JOIN FETCH c.bin "
			+ "WHERE c.bin.id IN "
			+ "(SELECT cl.bin.id "
			+ "FROM Closure cl "
			+ "WHERE cl.deleted = false "
			+ "GROUP BY cl.bin "
			+ "HAVING COUNT(cl.bin) >= :closureCount)")
	List<Closure> findClosureHasBinClosureOver(long closureCount);

	Optional<Presence> findPresenceByBinAndDeletedFalse(Bin bin);
}
