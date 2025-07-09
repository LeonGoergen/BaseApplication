package com.baseproject.service;

import com.baseproject.dto.*;
import com.baseproject.exception.*;
import com.baseproject.mapper.UserMapper;
import com.baseproject.model.User;
import jakarta.servlet.http.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final UserMapper userMapper;

  public UserDto login(LoginRequestDto requestDto, HttpServletRequest httpRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password())
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);

      HttpSession session = httpRequest.getSession(true);
      session.setAttribute(
          HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
          SecurityContextHolder.getContext()
      );
    } catch (Exception e) {
      log.warn("Invalid credentials for user: {}", requestDto.email(), e);
      throw new ValidationException(ExceptionEnum.INVALID_CREDENTIALS)
          .setHttpStatus(HttpStatus.UNAUTHORIZED);
    }

    User user = userService.findByEmail(requestDto.email());
    userService.updateLastActiveTime(user);

    log.info("User {} logged in successfully", user.getId());

    return userMapper.toDto(user);
  }

  public MessageDto logout(HttpServletRequest request)
  {
    HttpSession session = request.getSession(false);

    if (session == null) {
      throw new RessourceNotFoundException(ExceptionEnum.SESSION_NOT_FOUND)
          .setHttpStatus(HttpStatus.NOT_FOUND);
    }

    request.getSession().invalidate();
    SecurityContextHolder.clearContext();

    return new MessageDto(LocalDateTime.now(), "Logged out successfully");
  }

  public SessionInfoDto getSessionInfo(HttpServletRequest request) {
    HttpSession session = request.getSession(false);

    if (session == null) {
      throw new RessourceNotFoundException(ExceptionEnum.SESSION_NOT_FOUND)
          .setHttpStatus(HttpStatus.NOT_FOUND);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new ValidationException(ExceptionEnum.UNAUTHORIZED)
          .setHttpStatus(HttpStatus.UNAUTHORIZED);
    }

    return new SessionInfoDto(
        session.getId(),
        LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getCreationTime()), ZoneId.systemDefault()),
        LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getLastAccessedTime()), ZoneId.systemDefault()),
        session.getMaxInactiveInterval(),
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(session.getLastAccessedTime() + session.getMaxInactiveInterval() * 1000L),
            ZoneId.systemDefault()
        )
    );
  }
}
