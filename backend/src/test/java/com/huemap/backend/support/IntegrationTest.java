package com.huemap.backend.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.huemap.backend.infrastructure.s3.S3Uploader;

@Import(TestRedisConfig.class)
@SpringBootTest
public class IntegrationTest {

  @MockBean
  private S3Uploader s3Uploader;

}
