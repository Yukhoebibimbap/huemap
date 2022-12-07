package com.huemap.backend.domain.user.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import com.huemap.backend.common.exception.InvalidValueException;
import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.domain.user.dto.request.UserCreateRequest;
import com.huemap.backend.domain.user.dto.response.UserCreateResponse;
import com.huemap.backend.support.ControllerTest;

@DisplayName("UserController의")
public class UserControllerTest extends ControllerTest {

  @WithMockUser
  @Nested
  @DisplayName("save 메소드는")
  class save {

    @Nested
    @DisplayName("email이 null로 들어오면")
    class Context_with_null_email {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final UserCreateRequest request = new UserCreateRequest(null, "name", "Password1234!");

        //when
        final ResultActions perform = requestSave(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("email이 올바르지 않은 형식으로 들어오면")
    class Context_with_invalid_email {

      @ParameterizedTest
      @ValueSource(strings = {"huemap.com", "huemap.google.com"})
      @DisplayName("예외를 던진다.")
      void It_throws_exception(String email) throws Exception {
        //given
        final UserCreateRequest request = new UserCreateRequest(email, "name", "Password1234!");

        //when
        final ResultActions perform = requestSave(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("name이 null로 들어오면")
    class Context_with_null_name {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final UserCreateRequest request = new UserCreateRequest("huemap@gmail.com", null,
                                                                "Password1234!");

        //when
        final ResultActions perform = requestSave(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("name이 특수문자를 제외한 2~10자리가 아닌 형식으로 들어오면")
    class Context_with_invalid_name {

      @ParameterizedTest
      @ValueSource(strings = {"name!", "n", "nnnnnnnnnnn"})
      @DisplayName("예외를 던진다.")
      void It_throws_exception(String name) throws Exception {
        //given
        final UserCreateRequest request = new UserCreateRequest("huemap@gmail.com", name, "Password1234!");

        //when
        final ResultActions perform = requestSave(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("password가 null로 들어오면")
    class Context_with_null_password {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final UserCreateRequest request = new UserCreateRequest("huemap@gmail.com", "name",
                                                                null);

        //when
        final ResultActions perform = requestSave(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("password가 8~16자 영문 대소문자, 숫자, 특수문자를 사용한 형식이 아닌 상태로 들어오면")
    class Context_with_invalid_password {

      @ParameterizedTest
      @ValueSource(strings = {"password", "PASSWORD", "Password", "Password!", "Password1",
          "1111!!!!", "Pass1!", "PasswordPassword1!"})
      @DisplayName("예외를 던진다.")
      void It_throws_exception(String password) throws Exception {
        //given
        final UserCreateRequest request = new UserCreateRequest("huemap@gmail.com", "name", password);

        //when
        final ResultActions perform = requestSave(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("중복된 이메일에 대하여 회원가입을 한다면")
    class Context_with_already_exist {

      @Test
      @DisplayName("예외를 던진다.")
      void It_throws_exception() throws Exception {
        //given
        final UserCreateRequest request = new UserCreateRequest("huemap@gmail.com", "name", "Password1234!");
        willThrow(new InvalidValueException(ErrorCode.USER_EMAIL_DUPLICATED))
            .given(userService)
            .save(any(UserCreateRequest.class));

        //when
        final ResultActions perform = requestSave(request);

        //then
        perform.andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("email, name, password가 올바른 값으로 입력되면")
    class Context_with_valid_email_name_password {

      @Test
      @DisplayName("201을 응답한다.")
      void It_responses_201() throws Exception {
        //given
        final UserCreateRequest request = new UserCreateRequest("huemap@gmail.com", "name", "Password1234!");
        final UserCreateResponse response = new UserCreateResponse(1L);
        final RestResponse restResponse = RestResponse.of(response);
        given(userService.save(any(UserCreateRequest.class))).willReturn(response);

        //when
        final ResultActions perform = requestSave(request);

        //then
        perform.andExpect(status().isCreated())
               .andExpect(content().json(objectMapper.writeValueAsString(restResponse)));
      }
    }

    private ResultActions requestSave(final UserCreateRequest request)
        throws Exception {
      return requestPost("/api/v1/users", request);
    }
  }
}
