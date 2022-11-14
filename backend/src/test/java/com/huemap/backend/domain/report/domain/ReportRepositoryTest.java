package com.huemap.backend.domain.report.domain;

import static com.huemap.backend.support.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.support.RepositoryTest;

@DisplayName("ReportRepository의")
public class ReportRepositoryTest extends RepositoryTest {

  @Nested
  @DisplayName("findByUserIdAndBin 메소드는")
  class findByUserIdAndBin {

    @Nested
    @DisplayName("유저 id와 Bin 객체를 입력 받으면")
    class Context_with_userId_bin {

      @Test
      @DisplayName("이에 해당하는 Closure 엔티티를 반환한다.")
      void It_returns_closure() throws Exception {
        //given
        final Long userId = 1L;
        final Bin bin = binRepository.save(getBin());
        final Closure expected = (Closure)reportRepository.save(getClosure(bin));

        //when
        final Optional<Closure> actual = reportRepository.findClosureByUserIdAndBin(userId, bin);

        //then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(expected);
      }
    }
  }
}
