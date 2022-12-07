package com.huemap.backend.domain.user.dto.response;

public class AuthResponse {
  private String accessToken;
  private String grantType = "Bearer";
  private Long id;

  public AuthResponse(String accessToken, Long id) {
    this.accessToken = accessToken;
    this.id = id;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getGrantType() {
    return grantType;
  }

  public Long getId() {
    return id;
  }
}
