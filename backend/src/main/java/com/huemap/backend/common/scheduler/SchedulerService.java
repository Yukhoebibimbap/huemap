package com.huemap.backend.common.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.report.domain.Closure;
import com.huemap.backend.report.domain.Presence;
import com.huemap.backend.report.domain.ReportRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SchedulerService {

  private final ReportRepository reportRepository;

  private final long CLOSURE_COUNT = 3L;
  private final int PRESENCE_COUNT = 3;

  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
  @Transactional
  public void deleteBinHasClosureOver() {
    final LocalDateTime now = LocalDateTime.now();
    log.info("{}, 폐쇄 폐수거함 제보가 {}개 이상이면 폐수거함을 제거하는 스케줄러 실행", now, CLOSURE_COUNT);

    final List<Closure> closures = reportRepository.findClosureHasBinClosureOver(CLOSURE_COUNT);
    closures.forEach(Closure::delete);
    closures.forEach(c -> c.getBin().delete());
  }

  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
  @Transactional
  public void elevateBinHasPresenceOver() {
    final LocalDateTime now = LocalDateTime.now();
    log.info("{}, 존재 폐수거함 제보가 {}개 이상이면 후보 폐수거함을 본 폐수거함으로 승급시키는 스케줄러 실행", now, PRESENCE_COUNT);

    final List<Presence> presences = reportRepository.findPresenceByDeletedFalseAndCountGreaterThanEqual(PRESENCE_COUNT);
    presences.forEach(Presence::delete);
    presences.forEach(p -> p.getBin().elevate());
  }
}
