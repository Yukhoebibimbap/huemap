package com.huemap.backend.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

import com.huemap.backend.domain.user.domain.Role;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

  long id() default 1L;

  String email() default "test@test.com";

  String name() default "test";

  String password() default "password";

  Role role() default Role.ROLE_USER;

}
