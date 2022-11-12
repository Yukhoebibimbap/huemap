package com.huemap.backend.common.scheduler;

import static com.huemap.backend.common.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.huemap.backend.common.IntegrationTest;
import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.domain.bin.domain.BinRepository;
import com.huemap.backend.domain.report.domain.Closure;
import com.huemap.backend.domain.report.domain.Presence;
import com.huemap.backend.domain.report.domain.ReportRepository;

@DisplayName("SchedulerService의")
public class SchedulerServiceIntegrationTest extends IntegrationTest {

  static {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
  }

  @Autowired
  private SchedulerService schedulerService;

  @Autowired
  private BinRepository binRepository;

  @Autowired
  private ReportRepository reportRepository;

  @BeforeEach
  void init() {
    reportRepository.deleteAll();
    binRepository.deleteAll();
  }

  @Test
  @DisplayName("deleteBinHasClosureOver 스케줄러는 폐쇄 폐수거함 제보가 기준 값 이상인 폐수거함을 제거하는 로직을 실행한다.")
  void deleteBinHasClosureOverTest() throws Exception {
    //given
    final Bin bin = binRepository.save(getBin());
    for (long userId = 1; userId <= 3; userId++) {
      Closure closure = Closure.builder()
          .userId(userId)
          .bin(bin)
          .build();
      reportRepository.save(closure);
    }

    //when
    schedulerService.deleteBinHasClosureOver();

    //then
    final List<Closure> closures = reportRepository.findAll();
    assertAll(
        () -> assertThat(closures.get(0).isDeleted()).isTrue(),
        () -> assertThat(closures.get(1).isDeleted()).isTrue(),
        () -> assertThat(closures.get(2).isDeleted()).isTrue()
    );
    assertThat(binRepository.findAll().size()).isEqualTo(0);
  }

  @Test
  @DisplayName("elevateBinHasPresenceOver 스케줄러는 존재 폐수거함 제보가 기준 값 이상인 후보 폐수거함을 본 폐수거함으로 승급시키는 로직을 실행한다.")
  void elevateBinHasPresenceOverTest() throws Exception {
    //given
    final Bin bin = getBin();
    ReflectionTestUtils.setField(bin, "isCandidate", true);
    final Bin savedBin = binRepository.save(bin);
    final Presence presence = Presence.of(1L, savedBin);
    ReflectionTestUtils.setField(presence, "count", 3);
    reportRepository.save(presence);

    //when
    schedulerService.elevateBinHasPresenceOver();

    //then
    final Presence foundPresence = (Presence)reportRepository.findById(1L).get();
    assertThat(foundPresence.isDeleted()).isTrue();
    assertThat(binRepository.findAll().get(0).isCandidate()).isFalse();
  }
}
