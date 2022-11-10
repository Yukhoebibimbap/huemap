package com.huemap.backend.domain.bin.domain;

import static com.huemap.backend.common.TestUtils.getBin;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.domain.bin.domain.BinRepository;
import com.huemap.backend.domain.bin.domain.BinType;

@DataJpaTest
@DisplayName("BinRepository의")
public class BinRepositoryTest {

	@Autowired
	private BinRepository binRepository;

	@Nested
	@DisplayName("findAllByType 메소드는")
	class findAllByType {

		@Nested
		@DisplayName("BinType을 입력 받으면")
		class Context_with_binType {

			@Test
			@DisplayName("해당하는 type의 모든 Bin 엔티티를 반환한다.")
			void It_returns_allBins() throws Exception {
				//given
				binRepository.save(getBin());
				binRepository.save(getBin());
				final BinType type = BinType.CLOTHES;

				//when
				final List<Bin> actual = binRepository.findAllByType(type);

				//then
				assertThat(actual).hasSize(2);
			}
		}
	}

}
