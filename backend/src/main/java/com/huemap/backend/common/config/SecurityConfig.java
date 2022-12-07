package com.huemap.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.huemap.backend.common.security.AccessDeniedHandlerImpl;
import com.huemap.backend.common.security.CustomAuthenticationEntryPoint;
import com.huemap.backend.common.security.LoginFailureHandler;
import com.huemap.backend.common.security.LoginFilter;
import com.huemap.backend.common.security.LoginSuccessHandler;
import com.huemap.backend.common.security.TokenAuthenticationFilter;
import com.huemap.backend.common.security.TokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private static final String USER = "USER";

  private final TokenProvider tokenProvider;
  private final AuthenticationConfiguration authenticationConfiguration;

  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
    return new TokenAuthenticationFilter();
  }

  private LoginFilter loginFilter() throws Exception {
    LoginFilter loginFilter = new LoginFilter();
    loginFilter.setFilterProcessesUrl("/api/v1/users/login");
    loginFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
    loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
    loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(tokenProvider));
    return loginFilter;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .formLogin()
          .disable()
        .csrf()
          .disable()
        .headers()
          .disable()
        .httpBasic()
          .disable()
        .rememberMe()
          .disable()
        .logout()
          .disable()
        .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
        .exceptionHandling()
          .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
          .and()
        .exceptionHandling()
          .accessDeniedHandler(new AccessDeniedHandlerImpl())
          .and()
        .authorizeRequests()
          .antMatchers(HttpMethod.POST, "/api/v1/bins/{binId}/report-closures")
            .hasAnyRole(USER)
          .antMatchers(HttpMethod.POST, "/api/v1/bins/report-presences")
            .hasAnyRole(USER)
          .antMatchers(HttpMethod.PUT, "/api/v1/bins/{binId}/vote")
            .hasAnyRole(USER)
          .antMatchers(HttpMethod.POST, "/api/v1/bins/{binId}/report-condition")
            .hasAnyRole(USER)
          .antMatchers(HttpMethod.POST, "/api/v1/suggestions/bin-location")
            .hasAnyRole(USER)
          .anyRequest().permitAll()
          .and()
        .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
