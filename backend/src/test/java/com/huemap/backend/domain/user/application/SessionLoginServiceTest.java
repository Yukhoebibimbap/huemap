package com.huemap.backend.domain.user.application;

import static com.huemap.backend.support.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.huemap.backend.common.exception.EntityNotFoundException;
import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.domain.user.domain.User;
import com.huemap.backend.domain.user.domain.UserRepository;
import com.huemap.backend.domain.user.dto.request.UserLoginRequest;
import com.huemap.backend.domain.user.dto.response.UserLoginResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("SessionLoginService의")
public class SessionLoginServiceTest {

  @InjectMocks
  private SessionLoginService sessionLoginService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private HttpSession httpSession;

  @Nested
  @DisplayName("login 메소드는")
  class login {

    @Nested
    @DisplayName("사용자가 존재하지 않는 경우")
    class Context_with_not_found_user {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() {
        //given
        final UserLoginRequest request = new UserLoginRequest("huemap@gmail.com", "Password1234!");
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(
            () -> sessionLoginService.login(request))
            .isInstanceOf(EntityNotFoundException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.USER_NOT_FOUND);
      }
    }

    @Nested
    @DisplayName("비밀번호가 일치하지 않은 경우")
    class Context_with_not_match_password {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() {
        //given
        final UserLoginRequest request = new UserLoginRequest("huemap@gmail.com", "Notmatch1!");
        final User user = getUser();
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

        //when, then
        assertThatThrownBy(
            () -> sessionLoginService.login(request))
            .isInstanceOf(InvalidValueException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.LOGIN_PASSWORD_NOT_MATCH);
      }
    }

    @Nested
    @DisplayName("유효한 값이 넘어오면")
    class Context_with_valid_parameter {

      @Test
      @DisplayName("예외를 던진다.")
      void success() {
        //given
        final UserLoginRequest request = new UserLoginRequest("huemap@gmail.com", "Password1234!");
        final User user = getUser();
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

        //when
        final UserLoginResponse response = sessionLoginService.login(request);

        //then
        assertThat(response.getId()).isEqualTo(user.getId());
      }
    }
  }
}
