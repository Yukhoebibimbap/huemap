package com.huemap.backend.bin.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BinRepository extends JpaRepository<Bin, Long> {

	List<Bin> findAllByType(BinType type);

	@Query("SELECT b "
			+ "FROM Bin b "
			+ "WHERE b.id IN "
			+ "(SELECT c.bin.id "
			+ "FROM Closure c "
			+ "GROUP BY c.bin "
			+ "HAVING COUNT(c.bin) >= :closureCount)")
	List<Bin> findAllHasClosureOver(long closureCount);
}
