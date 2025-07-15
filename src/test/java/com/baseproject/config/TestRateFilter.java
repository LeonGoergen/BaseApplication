package com.baseproject.config;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class TestRateFilter {

  @Autowired
  private RateFilter rateFilter;

  @MockitoBean
  private FilterChain filterChain;

  @Test
  void allowsRequestWhenRateLimitNotExceeded() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    rateFilter.doFilterInternal(request, response, filterChain);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
  }

  @Test
  void blocksRequestWhenRateLimitExceeded() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    for (int i = 0; i < 21; i++) {
      rateFilter.doFilterInternal(request, response, filterChain);
    }

    assertThat(response.getStatus()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value());
    assertThat(response.getContentType()).isEqualTo("application/json");
  }

  @Test
  void handlesInvalidRequestGracefully() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI(null);
    MockHttpServletResponse response = new MockHttpServletResponse();

    rateFilter.doFilterInternal(request, response, filterChain);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
  }
}