package com.huemap.backend.domain.user.application;

import static com.huemap.backend.support.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.domain.user.domain.User;
import com.huemap.backend.domain.user.domain.UserRepository;
import com.huemap.backend.domain.user.dto.request.UserCreateRequest;
import com.huemap.backend.domain.user.dto.response.UserCreateResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService의")
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Nested
  @DisplayName("save 메소드는")
  class save {

    @Nested
    @DisplayName("이미 같은 이메일이 있는 상태에서 회원가입을 한다면")
    class Context_with_already_exist_email {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() {
        //given
        final UserCreateRequest request = new UserCreateRequest("huemap@gmail.com", "name",
                                                                "Password1234!");
        given(userRepository.existsByEmail(anyString())).willReturn(true);

        // when, then
        assertThatThrownBy(
            () -> userService.save(request))
            .isInstanceOf(InvalidValueException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.USER_EMAIL_DUPLICATED);
      }
    }

    @Nested
    @DisplayName("유효한 값이 넘어오면")
    class Context_with_valid_argument {

      @Test
      @DisplayName("유저를 저장한다.")
      void success() {
        //given
        final UserCreateRequest request = new UserCreateRequest("huemap@gmail.com", "name",
                                                                "Password1234!");
        final User user = getUser();
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(user);

        //when
        final UserCreateResponse response = userService.save(request);

        //then
        verify(userRepository).save(any(User.class));
        assertThat(response.getId()).isEqualTo(user.getId());
      }
    }
  }
}
