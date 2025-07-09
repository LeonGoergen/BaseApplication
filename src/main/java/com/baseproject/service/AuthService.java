package com.baseproject.service;

import com.baseproject.dto.*;
import com.baseproject.exception.*;
import com.baseproject.mapper.UserMapper;
import com.baseproject.model.*;
import com.baseproject.repository.EmailVerificationTokenRepository;
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
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final MailService mailService;
  private final UserMapper userMapper;

  private final EmailVerificationTokenRepository emailVerificationTokenRepository;

  public UserDto login(LoginRequestDto requestDto, HttpServletRequest httpRequest) {
    User user = userService.findByEmail(requestDto.email());

    if (user.getIsVerified().equals(Boolean.FALSE)) {
      log.warn("User {} is not verified", user.getId());
      throw new ValidationException(ExceptionEnum.USER_NOT_VERIFIED)
          .setHttpStatus(HttpStatus.UNAUTHORIZED)
          .setReference("User " + user.getEmail() + " is not verified");
    }

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

  public void verifyEmail(String token) {
    EmailVerificationToken verificationToken = emailVerificationTokenRepository.findById(UUID.fromString(token))
        .orElseThrow(() -> new RessourceNotFoundException(ExceptionEnum.EMAIL_VERIFICATION_TOKEN_NOT_FOUND));

    if (verificationToken.isExpired()) {
      log.warn("Email verification token {} is expired", token);
      throw new ValidationException(ExceptionEnum.EMAIL_VERIFICATION_TOKEN_EXPIRED)
          .setHttpStatus(HttpStatus.GONE)
          .setReference("Email verification token " + token + " is expired");
    }

    User user = verificationToken.getUser();

    if (user.getIsVerified()) {
      log.warn("User {} is already verified", user.getId());
      throw new ValidationException(ExceptionEnum.USER_ALREADY_VERIFIED)
          .setHttpStatus(HttpStatus.BAD_REQUEST)
          .setReference("User " + user.getEmail() + " is already verified");
    }

    user.setIsVerified(true);
    userService.save(user);

    emailVerificationTokenRepository.delete(verificationToken);

    log.info("User {} verified successfully", user.getId());
  }

  public void resendConfirmationEmail(String email) {
    User user = userService.findByEmail(email);

    if (user.getIsVerified()) {
      log.warn("User {} is already verified", user.getId());
      throw new ValidationException(ExceptionEnum.USER_ALREADY_VERIFIED)
          .setHttpStatus(HttpStatus.BAD_REQUEST)
          .setReference("User " + user.getEmail() + " is already verified");
    }

    emailVerificationTokenRepository.deleteByUser(user);

    EmailVerificationToken token = new EmailVerificationToken(user);
    token = emailVerificationTokenRepository.save(token);

    mailService.sendRegistrationMail(user.getEmail(), user.getFirstName(), token.getId());
  }
}
