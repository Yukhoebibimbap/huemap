package com.huemap.backend.support;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huemap.backend.common.config.RedisConfig;
import com.huemap.backend.common.resolver.AuthInterceptor;
import com.huemap.backend.domain.bin.application.BinService;
import com.huemap.backend.domain.bin.presentation.BinController;
import com.huemap.backend.domain.report.application.ReportService;
import com.huemap.backend.domain.report.presentation.ReportController;
import com.huemap.backend.domain.user.application.LoginService;
import com.huemap.backend.domain.user.application.UserService;
import com.huemap.backend.domain.user.presentation.UserController;

@Import({TestRedisConfig.class, RedisConfig.class})
@WebMvcTest({
    BinController.class,
    ReportController.class,
    UserController.class
})
@MockBean(JpaMetamodelMappingContext.class)
public class ControllerTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockBean
  protected BinService binService;

  @MockBean
  protected ReportService reportService;

  @MockBean
  protected UserService userService;

  @MockBean
  protected LoginService loginService;

  @MockBean
  protected AuthInterceptor authInterceptor;

  @BeforeEach
  void initEach() {
    given(authInterceptor.preHandle(any(), any(), any())).willReturn(true);
  }
}
