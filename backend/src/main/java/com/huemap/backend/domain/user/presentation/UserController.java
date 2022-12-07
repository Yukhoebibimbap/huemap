package com.huemap.backend.domain.user.presentation;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.domain.user.application.UserService;
import com.huemap.backend.domain.user.dto.request.UserCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public RestResponse save(
      @RequestBody @Valid UserCreateRequest userCreateRequest
  ) {
    return RestResponse.of(userService.save(userCreateRequest));
  }
}
