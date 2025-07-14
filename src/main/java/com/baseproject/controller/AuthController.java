package com.baseproject.controller;

import com.baseproject.api.AuthApi;
import com.baseproject.dto.*;
import com.baseproject.mapper.UserMapper;
import com.baseproject.model.User;
import com.baseproject.service.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final AuthService authService;

  private final UserService userService;
  private final UserMapper userMapper;

  private final HttpServletRequest httpRequest;

  @Override
  @PostMapping("/register")
  public ResponseEntity<UserDto> register(@RequestBody @Validated UserCreateDto user) {
    User userDto = userService.create(user);
    return ResponseEntity.ok(userMapper.toDto(userDto));
  }

  @Override
  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request) {
    UserDto user = authService.login(request, httpRequest);
    return ResponseEntity.ok(user);
  }

  @Override
  @PostMapping("/logout")
  public ResponseEntity<MessageDto> logout() {
    MessageDto message = authService.logout(httpRequest);
    return ResponseEntity.ok(message);
  }

  @Override
  @GetMapping("/info")
  public ResponseEntity<SessionInfoDto> getSessionInfo() {
    SessionInfoDto sessionInfo = authService.getSessionInfo(httpRequest);
    return ResponseEntity.ok(sessionInfo);
  }

  @Override
  @GetMapping("/confirm-email")
  public ResponseEntity<MessageDto> confirmEmail(@RequestParam("token") String token) {
    authService.verifyEmail(token);
    return ResponseEntity.accepted().build();
  }

  // # TODO: how to reach this endpoint?
  @Override
  @PostMapping("/resend-confirmation")
  public ResponseEntity<MessageDto> resendConfirmation(@RequestBody EmailDto emailDto) {
    authService.resendConfirmationEmail(emailDto);
    return ResponseEntity.ok().build();
  }

  @Override
  @PostMapping("/forgot-password")
  public ResponseEntity<MessageDto> forgotPassword(@RequestBody EmailDto emailDto) {
    authService.sendPasswordResetEmail(emailDto);
    return ResponseEntity.ok().build();
  }

  @Override
  @PostMapping("/reset-password")
  public ResponseEntity<MessageDto> resetPassword(@RequestBody PasswordResetRequestDto request) {
    authService.resetPassword(request);
    return ResponseEntity.ok().build();
  }
}

