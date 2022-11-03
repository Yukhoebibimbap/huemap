package com.huemap.backend.bin.domain;

import static com.huemap.backend.common.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.huemap.backend.report.domain.Closure;
import com.huemap.backend.report.domain.ReportRepository;

@DataJpaTest
@DisplayName("BinRepository의")
public class BinRepositoryTest {

  @Autowired
  private BinRepository binRepository;

  @Autowired
  private ReportRepository reportRepository;

  @Nested
  @DisplayName("findAllHasClosureOver 메소드는")
  class findAllHasClosureOver {

    @Nested
    @DisplayName("폐쇄 제보 기준 값을 입력받으면")
    class Context_with_closure_count {

      @Test
      @DisplayName("해당 개수를 넘은 Bin 엔티티를 반환한다.")
      void It_returns_closure() throws Exception {
        //given
        final long closureCount = 1L;
        final Bin bin = binRepository.save(getBin());
        reportRepository.save(getClosure(bin));

        //when
        final List<Bin> bins = binRepository.findAllHasClosureOver(closureCount);

        //then
        assertThat(bins.size()).isEqualTo(1);
        assertThat(bins.get(0).getId()).isEqualTo(bin.getId());
      }
    }
  }
}
