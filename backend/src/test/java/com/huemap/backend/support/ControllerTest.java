package com.huemap.backend.support;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huemap.backend.common.config.RedisConfig;
import com.huemap.backend.domain.bin.application.BinService;
import com.huemap.backend.domain.bin.presentation.BinController;
import com.huemap.backend.domain.report.application.ReportService;
import com.huemap.backend.domain.report.presentation.ReportController;
import com.huemap.backend.domain.suggestion.application.SuggestionService;
import com.huemap.backend.domain.suggestion.presentation.SuggestionController;
import com.huemap.backend.domain.user.application.LoginService;
import com.huemap.backend.domain.user.application.UserService;
import com.huemap.backend.domain.user.presentation.UserController;

@Import({TestRedisConfig.class, RedisConfig.class})
@WebMvcTest({
    BinController.class,
    ReportController.class,
    UserController.class,
    SuggestionController.class
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
  protected SuggestionService suggestionService;

  protected ResultActions requestGet(final String url) throws Exception {
    return mockMvc.perform(get(url))
                  .andDo(print());
  }

  protected ResultActions requestPost(final String url, final Object request) throws Exception {
    final String content = objectMapper.writeValueAsString(request);

    return mockMvc.perform(post(url)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                  .andDo(print());
  }

  protected ResultActions requestPut(final String url, final Object request) throws Exception {
    final String content = objectMapper.writeValueAsString(request);

    return mockMvc.perform(put(url)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                  .andDo(print());
  }
}
