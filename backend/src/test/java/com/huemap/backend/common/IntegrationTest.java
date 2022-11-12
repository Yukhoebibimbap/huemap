package com.huemap.backend.common;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.huemap.backend.infrastructure.s3.S3Uploader;

@SpringBootTest
public class IntegrationTest {

  @MockBean
  private S3Uploader s3Uploader;

}
