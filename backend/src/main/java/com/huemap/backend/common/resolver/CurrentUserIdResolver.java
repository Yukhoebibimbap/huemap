package com.huemap.backend.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.huemap.backend.common.annotation.CurrentUserId;
import com.huemap.backend.domain.user.application.LoginService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CurrentUserIdResolver implements HandlerMethodArgumentResolver {

  private final LoginService loginService;

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.hasParameterAnnotation(CurrentUserId.class);
  }

  @Override
  public Long resolveArgument(MethodParameter methodParameter,
                                ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest,
                                WebDataBinderFactory webDataBinderFactory) throws Exception {
    Long currentUserId = loginService.getCurrentUser();
    return currentUserId;
  }
}
