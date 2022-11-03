package com.huemap.backend.common.scheduler;

import static com.huemap.backend.common.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.huemap.backend.bin.domain.Bin;
import com.huemap.backend.bin.domain.BinRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("SchedulerService의")
public class SchedulerServiceTest {

  @InjectMocks
  private SchedulerService schedulerService;

  @Mock
  private BinRepository binRepository;

  @Nested
  @DisplayName("deleteBinHasClosureOver 메소드는")
  class deleteBinHasClosureOver {

    @Test
    @DisplayName("폐쇄 제보가 일정 수 이상인 폐수거함을 제거한다.")
    void It_throws_exception() throws Exception {
      //given
      final Bin bin = getBin();
      given(binRepository.findAllHasClosureOver(anyLong())).willReturn(List.of(bin));

      // when
      schedulerService.deleteBinHasClosureOver();

      // then
      verify(binRepository).findAllHasClosureOver(anyLong());
      assertThat(bin.isDeleted()).isTrue();
    }
  }
}
