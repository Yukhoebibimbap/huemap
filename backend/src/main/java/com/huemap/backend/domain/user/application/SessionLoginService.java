package com.huemap.backend.domain.user.application;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.domain.user.domain.User;
import com.huemap.backend.domain.user.domain.UserMapper;
import com.huemap.backend.domain.user.domain.UserRepository;
import com.huemap.backend.domain.user.dto.request.UserLoginRequest;
import com.huemap.backend.domain.user.dto.response.UserLoginResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionLoginService implements LoginService{

  private final String LOGIN_USER_ID = "USER_ID";
  private final UserRepository userRepository;
  private final HttpSession httpSession;

  @Override
  public UserLoginResponse login(UserLoginRequest userLoginRequest) {
    final User user = userRepository.findByEmail(userLoginRequest.getEmail())
                                    .orElseThrow(
                                     () -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

    user.matchPassword(userLoginRequest.getPassword());

    httpSession.setAttribute(LOGIN_USER_ID, user.getId());

    return UserMapper.INSTANCE.toLoginDto(user);
  }

  @Override
  public Long getCurrentUser() {
    return (Long)httpSession.getAttribute(LOGIN_USER_ID);
  }
}
