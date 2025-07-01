package com.baseproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.*;

import java.io.*;

@Service
@Log4j2
public class LoggingFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain)
                                  throws ServletException, IOException {

    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

    String requestURI = request.getRequestURI();
    String method = request.getMethod();

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    try {
      filterChain.doFilter(wrappedRequest, wrappedResponse);
    } finally {
      stopWatch.stop();

      String requestBody = getRequestBody(wrappedRequest);
      String responseBody = getResponseBody(wrappedResponse);

      log.info("→ [{} {}] Body: {}", method, requestURI, requestBody);
      log.info("← [Status: {}] Body: {} ({} ms)", wrappedResponse.getStatus(), responseBody, stopWatch.getTotalTimeMillis());

      wrappedResponse.copyBodyToResponse();
    }
  }

  private String getRequestBody(ContentCachingRequestWrapper request) {
    byte[] buf = request.getContentAsByteArray();
    if (buf.length == 0) return "";
    try {
      String raw = new String(buf, 0, buf.length, request.getCharacterEncoding());
      return compactJson(raw);
    } catch (UnsupportedEncodingException e) {
      return "[Unsupported Encoding]";
    }
  }

  private String getResponseBody(ContentCachingResponseWrapper response) {
    byte[] buf = response.getContentAsByteArray();
    if (buf.length == 0) return "";
    try {
      String raw = new String(buf, 0, buf.length, response.getCharacterEncoding());
      return compactJson(raw);
    } catch (UnsupportedEncodingException e) {
      return "[Unsupported Encoding]";
    }
  }

  private String compactJson(String raw) {
    try {
      if (raw == null || raw.isBlank()) return "";
      Object json = objectMapper.readValue(raw, Object.class);
      return objectMapper.writeValueAsString(json);
    } catch (IOException e) {
      return raw.replaceAll("[\\n\\r]+", " ").trim();
    }
  }
}

