package com.huemap.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.huemap.backend.common.config.JwtConfig;
import com.huemap.backend.infrastructure.socket.config.SocketIOConfig;

@EnableConfigurationProperties({
    JwtConfig.class,
    SocketIOConfig.class
})
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class BackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }

}
