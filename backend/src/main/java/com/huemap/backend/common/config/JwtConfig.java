package com.huemap.backend.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
public class JwtConfig {
  private final String header;
  private final String issuer;
  private final String clientSecret;
  private final int expirySeconds;

  public JwtConfig(String header, String issuer, String clientSecret, int expirySeconds) {
    this.header = header;
    this.issuer = issuer;
    this.clientSecret = clientSecret;
    this.expirySeconds = expirySeconds;
  }

  public String getHeader() {
    return header;
  }

  public String getIssuer() {
    return issuer;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public int getExpirySeconds() {
    return expirySeconds;
  }

}
