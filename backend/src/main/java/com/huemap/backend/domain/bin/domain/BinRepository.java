package com.huemap.backend.domain.bin.domain;

import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinRepository extends JpaRepository<Bin, Long> {

	List<Bin> findAllByType(BinType type);

	Optional<Bin> findBinByTypeAndLocation(BinType type, Point location);

}
