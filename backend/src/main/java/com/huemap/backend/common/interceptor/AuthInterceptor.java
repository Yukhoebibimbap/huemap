package com.huemap.backend.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.huemap.backend.common.annotation.AuthRequired;
import com.huemap.backend.common.exception.AuthException;
import com.huemap.backend.common.response.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }

    HandlerMethod handlerMethod = (HandlerMethod)handler;
    AuthRequired authRequired = handlerMethod.getMethodAnnotation(AuthRequired.class);
    if (authRequired == null) {
      return true;
    }

    HttpSession httpSession = request.getSession();
    Long userId = (Long)httpSession.getAttribute("USER_ID");
    if (userId == null) {
      throw new AuthException(ErrorCode.AUTH_ERROR);
    }

    return true;
  }
}
