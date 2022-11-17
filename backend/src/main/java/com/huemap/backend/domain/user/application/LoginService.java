package com.huemap.backend.domain.user.application;

import com.huemap.backend.domain.user.dto.request.UserLoginRequest;
import com.huemap.backend.domain.user.dto.response.UserLoginResponse;

public interface LoginService {

  UserLoginResponse login(UserLoginRequest userLoginRequest);

  Long getCurrentUser();
}
