package com.huemap.backend.support;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.test.util.ReflectionTestUtils;

import com.huemap.backend.common.security.UserPrincipal;
import com.huemap.backend.domain.user.domain.User;

public class WithMockCustomUserSecurityContextFactory
    implements WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
    final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

    final User user = User.builder()
                          .email(annotation.email())
                          .name(annotation.name())
                          .password(annotation.password())
                          .build();
    ReflectionTestUtils.setField(user, "id", annotation.id());

    final UserPrincipal userPrincipal = UserPrincipal.create(user);
    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            userPrincipal, "password", userPrincipal.getAuthorities());

    securityContext.setAuthentication(authenticationToken);

    return securityContext;
  }
}
