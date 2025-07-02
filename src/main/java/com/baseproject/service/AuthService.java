package com.baseproject.service;

import com.baseproject.dto.*;
import com.baseproject.exception.*;
import jakarta.servlet.http.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
@AllArgsConstructor
@Log4j2
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;

  public UserDto login(LoginRequestDto requestDto, HttpServletRequest httpRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(requestDto.username(), requestDto.password())
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);

      HttpSession session = httpRequest.getSession(true);
      session.setAttribute(
          HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
          SecurityContextHolder.getContext()
      );

    } catch (BadCredentialsException e) {
      throw new ValidationException(ExceptionEnum.INVALID_CREDENTIALS)
          .setHttpStatus(HttpStatus.UNAUTHORIZED);
    }

    UserDto user = userService.findDtoByUsername(requestDto.username());

    log.info("User {} logged in successfully", requestDto.username());

    return user;
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
