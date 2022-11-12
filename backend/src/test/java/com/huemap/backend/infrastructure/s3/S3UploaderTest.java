package com.huemap.backend.infrastructure.s3;

import io.findify.s3mock.S3Mock;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.huemap.backend.common.IntegrationTest;

@ActiveProfiles("test")
@Import({S3MockConfig.class})
class S3UploaderTest extends IntegrationTest {

  @Autowired
  private AmazonS3 amazonS3;

  @Autowired
  private static final String BUCKET_NAME = "huemap-test";

  @BeforeAll
  static void setUp(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
    s3Mock.start();
    amazonS3.createBucket(BUCKET_NAME);
  }

  @AfterAll
  static void tearDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
    amazonS3.shutdown();
    s3Mock.stop();
  }

  @Test
  void upload() throws IOException {
    // given
    String path = "test/02.txt";
    String contentType = "text/plain";
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(contentType);
    objectMetadata.setContentLength(objectMetadata.getContentLength());
    PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, path,
                                                             new ByteArrayInputStream("".getBytes(
                                                                 StandardCharsets.UTF_8)),
                                                             objectMetadata);
    amazonS3.putObject(putObjectRequest);

    // when
    S3Object s3Object = amazonS3.getObject(BUCKET_NAME, path);

    // then
    assertThat(s3Object.getObjectMetadata().getContentType()).isEqualTo(contentType);
    assertThat(new String(FileCopyUtils.copyToByteArray(s3Object.getObjectContent()))).isEqualTo("");
  }
}
