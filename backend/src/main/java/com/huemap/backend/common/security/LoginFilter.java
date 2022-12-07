package com.huemap.backend.common.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huemap.backend.common.exception.AuthException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.domain.user.dto.request.UserLoginRequest;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
				"Authentication method not supported: " + request.getMethod());
		}

		UserLoginRequest loginRequest;
		try {
			loginRequest = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);
		} catch (IOException e) {
			throw new AuthException(ErrorCode.BAD_LOGIN);
		}

		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
			username, password);

		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}
}