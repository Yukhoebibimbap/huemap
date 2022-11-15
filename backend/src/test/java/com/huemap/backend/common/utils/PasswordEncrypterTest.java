package com.huemap.backend.common.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PasswordEncrypter의")
public class PasswordEncrypterTest {

  @Nested
  @DisplayName("encrypt 메소드는")
  class encrypt {

    @Nested
    @DisplayName("password가 주어지면")
    class Context_with_password {

      @Test
      @DisplayName("암호화된 해시값을 반환한다.")
      void It_encrypted_password() {
        //given
        final String password = "Password1234!";

        //when
        final String encryptedPassword = PasswordEncrypter.encrypt(password);

        //then
        assertThat(PasswordEncrypter.isMatch(password, encryptedPassword)).isTrue();
      }
    }
  }
}
