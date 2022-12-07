package com.huemap.backend.domain.user.dto.response;

public class AuthResponse {
  private String accessToken;
  private String grantType = "Bearer";

  public AuthResponse(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getGrantType() {
    return grantType;
  }
}
