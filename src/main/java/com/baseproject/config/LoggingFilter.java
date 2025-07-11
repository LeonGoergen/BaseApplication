package com.baseproject.config;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.*;

import java.io.*;
import java.util.Map;

@Service
@Slf4j
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

    String requestBody = getRequestBody(wrappedRequest);

    StringBuilder logMessageIn = new StringBuilder();
    logMessageIn.append("→ [").append(method).append(" ").append(requestURI).append("] ");
    if (!requestBody.isEmpty()) {
      logMessageIn.append("Body: ").append(requestBody);
    }

    log.info(logMessageIn.toString());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    try {
      filterChain.doFilter(wrappedRequest, wrappedResponse);
    } finally {
      stopWatch.stop();

      String responseBody = getResponseBody(wrappedResponse);

      StringBuilder logMessageOut = new StringBuilder();
      logMessageOut.append("← [Status: ").append(wrappedResponse.getStatus()).append("] ");
      if (!responseBody.isEmpty()) {
        logMessageOut.append("Body: ").append(responseBody).append(" (").append(stopWatch.getTotalTimeMillis()).append(" ms)");
      } else {
        logMessageOut.append("No response body (").append(stopWatch.getTotalTimeMillis()).append(" ms)");
      }

      log.info(logMessageOut.toString());

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
    if (raw == null || raw.isBlank()) return "";

    try {
      JsonNode root = objectMapper.readTree(raw);
      if (root.isObject()) {
        ObjectNode objectNode = (ObjectNode) root;
        if (objectNode.has("password")) {
          objectNode.put("password", "*****");
        }
      }
      return objectMapper.writeValueAsString(root);
    } catch (IOException e) {
      return raw.replaceAll("[\\n\\r]+", " ").trim();
    }
  }
}

