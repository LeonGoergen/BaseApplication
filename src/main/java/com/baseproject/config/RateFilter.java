package com.baseproject.config;

import com.baseproject.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.*;

import static com.baseproject.exception.ExceptionEnum.TOO_MANY_REQUESTS;

@Service
@Slf4j
public class RateFilter extends OncePerRequestFilter {

  private final Bucket bucket;
  private final ObjectMapper objectMapper;

  public RateFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;

    Bandwidth bandwidth = BandwidthBuilder.builder()
        .capacity(20)
        .refillGreedy(1, Duration.ofSeconds(1))
        .initialTokens(20)
        .build();

    this.bucket = Bucket.builder()
        .addLimit(bandwidth)
        .build();
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    if (!bucket.tryConsume(1))
    {
      log.warn("Rate limit exceeded for request: {}", request.getRequestURI());

      ErrorResponseDto errorResponse = new ErrorResponseDto(
          OffsetDateTime.now(),
          TOO_MANY_REQUESTS.getCode(),
          TOO_MANY_REQUESTS.getMessage()
      );

      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      response.setContentType("application/json");
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
      response.getWriter().flush();
      response.getWriter().close();
      return;
    }

    filterChain.doFilter(request, response);
  }
}
