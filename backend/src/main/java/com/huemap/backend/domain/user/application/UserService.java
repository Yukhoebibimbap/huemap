package com.huemap.backend.domain.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.domain.user.domain.User;
import com.huemap.backend.domain.user.domain.UserMapper;
import com.huemap.backend.domain.user.domain.UserRepository;
import com.huemap.backend.domain.user.dto.request.UserCreateRequest;
import com.huemap.backend.domain.user.dto.response.UserCreateResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public UserCreateResponse save(UserCreateRequest userCreateRequest) {
    validateEmailAlreadyExist(userCreateRequest.getEmail());

    final User user = userRepository.save(UserMapper.INSTANCE.toEntity(userCreateRequest));

    return UserMapper.INSTANCE.toCreateDto(user);
  }

  private void validateEmailAlreadyExist(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new InvalidValueException(ErrorCode.USER_EMAIL_DUPLICATED);
    }
  }
}
