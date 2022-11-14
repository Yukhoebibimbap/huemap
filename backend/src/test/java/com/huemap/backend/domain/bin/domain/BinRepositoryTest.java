package com.huemap.backend.domain.bin.domain;

import static com.huemap.backend.support.TestUtils.getBin;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.huemap.backend.support.RepositoryTest;

@DisplayName("BinRepository의")
public class BinRepositoryTest extends RepositoryTest {

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
