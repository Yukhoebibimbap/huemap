package com.huemap.backend.infrastructure.socket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.SocketIOServer;

@ConfigurationProperties(prefix = "socket")
@Configuration
public class SocketIOConfig {

	@Value("${socket.host}")
	private String host;

	@Value("${socket.port}")
	private Integer port;

	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		config.setHostname(host);
		config.setPort(port);
		config.setContext("/socket.io");
		return new SocketIOServer(config);
	}

}
