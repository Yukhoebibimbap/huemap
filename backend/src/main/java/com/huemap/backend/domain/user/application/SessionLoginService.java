package com.huemap.backend.domain.user.application;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.domain.user.dto.request.UserLoginRequest;
import com.huemap.backend.domain.user.dto.response.UserLoginResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionLoginService implements LoginService{

  private final HttpSession httpSession;

  @Override
  public UserLoginResponse login(UserLoginRequest userLoginRequest) {
    return null;
  }
}
