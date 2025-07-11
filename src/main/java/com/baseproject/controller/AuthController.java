package com.baseproject.controller;

import com.baseproject.dto.*;
import com.baseproject.service.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request, HttpServletRequest httpRequest) {
    UserDto user = authService.login(request, httpRequest);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/logout")
  public ResponseEntity<MessageDto> logout(HttpServletRequest request, HttpServletResponse response) {
    MessageDto message = authService.logout(request);
    return ResponseEntity.ok(message);
  }

  @GetMapping("/info")
  public ResponseEntity<SessionInfoDto> sessionInfo(HttpServletRequest request) {
    SessionInfoDto sessionInfo = authService.getSessionInfo(request);
    return ResponseEntity.ok(sessionInfo);
  }

  @GetMapping("/confirm-email")
  public ResponseEntity<MessageDto> verifyEmail(@RequestParam("token") String token) {
    authService.verifyEmail(token);
    return ResponseEntity.accepted().build();
  }

  // # TODO: how to reach this endpoint?
  @PostMapping("/resend-confirmation")
  public ResponseEntity<MessageDto> resendConfirmationEmail(@RequestBody EmailDto emailDto) {
    authService.resendConfirmationEmail(emailDto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<MessageDto> sendPasswordResetEMail(@RequestBody EmailDto emailDto) {
    authService.sendPasswordResetEmail(emailDto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<MessageDto> resetPassword(@RequestBody PasswordResetRequestDto request) {
    authService.resetPassword(request);
    return ResponseEntity.ok().build();
  }
}

