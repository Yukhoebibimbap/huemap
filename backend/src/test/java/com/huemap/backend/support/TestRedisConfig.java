package com.huemap.backend.support;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;

import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@Slf4j
@TestConfiguration
public class TestRedisConfig {

  private RedisServer redisServer;

  public TestRedisConfig(@Value("${spring.redis.port}") int redisPort) {
    redisServer = RedisServer.builder()
        .port(redisPort)
        .setting("maxmemory 128M")
        .build();
  }

  @PostConstruct
  public void startRedis() {
    try {
      redisServer.start();
    } catch (Exception e) {
      log.error("", e);
    }
  }

  @PreDestroy
  public void stopRedis() {
    redisServer.stop();
  }

}
