package com.huemap.backend.report.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huemap.backend.bin.domain.Bin;

public interface ReportRepository<T extends Report> extends JpaRepository<T, Long> {

  Optional<Closure> findByUserIdAndBin(Long userId, Bin bin);
}
