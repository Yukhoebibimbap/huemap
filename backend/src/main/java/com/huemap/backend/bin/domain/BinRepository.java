package com.huemap.backend.bin.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BinRepository extends JpaRepository<Bin,Long> {
	List<Bin> findAllByType(BinType type);

}
