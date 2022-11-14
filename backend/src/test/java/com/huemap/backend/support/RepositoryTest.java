package com.huemap.backend.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.huemap.backend.common.config.RedisConfig;
import com.huemap.backend.domain.bin.domain.BinRepository;
import com.huemap.backend.domain.report.domain.ReportRepository;

@Import({TestRedisConfig.class, RedisConfig.class})
@DataJpaTest
public class RepositoryTest {

  @Autowired
  protected ReportRepository reportRepository;

  @Autowired
  protected BinRepository binRepository;

}
