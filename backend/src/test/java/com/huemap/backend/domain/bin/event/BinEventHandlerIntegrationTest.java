package com.huemap.backend.domain.bin.event;

import static com.huemap.backend.common.utils.GeometryUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.domain.bin.domain.BinRepository;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.bin.event.BinCreateEvent;
import com.huemap.backend.domain.bin.event.BinEventHandler;

@SpringBootTest
@DisplayName("BinEventHandler의")
public class BinEventHandlerIntegrationTest {

  @Autowired
  private BinEventHandler binEventHandler;

  @Autowired
  private BinRepository binRepository;

  @Nested
  @DisplayName("createCandidateBin 메소드는")
  class createCandidateBin {

    @Nested
    @DisplayName("폐수거함 생성 이벤트가 발생하면")
    class Context_with_bin_create_event {

      @Test
      @DisplayName("후보 폐수거함이 저장된다.")
      void It_save_candidate_bin() {
        //given
        final BinCreateEvent binCreateEvent = BinCreateEvent.candidateOf(BinType.CLOTHES,
                                                                         convertPoint(37.583297,
                                                                                      126.987755));

        //when
        binEventHandler.createCandidateBin(binCreateEvent);

        //then
        final Bin bin = binRepository.findAll().get(0);
        assertAll(
            () -> assertThat(bin.getType()).isEqualTo(BinType.CLOTHES),
            () -> assertThat(bin.getLocation().getY()).isEqualTo(37.583297),
            () -> assertThat(bin.getLocation().getX()).isEqualTo(126.987755),
            () -> assertThat(bin.isCandidate()).isTrue()
        );
      }
    }
  }
}
