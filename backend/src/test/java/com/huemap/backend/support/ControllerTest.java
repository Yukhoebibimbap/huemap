package com.huemap.backend.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huemap.backend.common.config.RedisConfig;
import com.huemap.backend.domain.bin.application.BinService;
import com.huemap.backend.domain.bin.presentation.BinController;
import com.huemap.backend.domain.report.application.ReportService;
import com.huemap.backend.domain.report.presentation.ReportController;

@Import({TestRedisConfig.class, RedisConfig.class})
@WebMvcTest({
    BinController.class,
    ReportController.class
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

}