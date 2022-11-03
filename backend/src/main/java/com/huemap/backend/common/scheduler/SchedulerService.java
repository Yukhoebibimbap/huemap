package com.huemap.backend.common.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.bin.domain.Bin;
import com.huemap.backend.bin.domain.BinRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SchedulerService {

  private final BinRepository binRepository;

  private final long CLOSURE_COUNT = 3L;

  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
  @Transactional
  public void deleteBinHasClosureOver() {
    final LocalDateTime now = LocalDateTime.now();
    log.info("{}, 폐쇄 폐수거함 제보가 {}개 이상이면 폐수거함을 제거하는 스케줄러 실행", now, CLOSURE_COUNT);

    binRepository.findAllHasClosureOver(CLOSURE_COUNT)
                 .forEach(Bin::delete);
  }
}
