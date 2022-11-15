package com.huemap.backend.domain.user.presentation;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.domain.user.application.LoginService;
import com.huemap.backend.domain.user.application.UserService;
import com.huemap.backend.domain.user.dto.request.UserCreateRequest;
import com.huemap.backend.domain.user.dto.request.UserLoginRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final LoginService loginService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public RestResponse save(
      @RequestBody @Valid UserCreateRequest userCreateRequest
  ) {
    return RestResponse.of(userService.save(userCreateRequest));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("login")
  public RestResponse login(
      @RequestBody @Valid UserLoginRequest userLoginRequest
  ) {
    return RestResponse.of(loginService.login(userLoginRequest));
  }
}
