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

  @GetMapping("/confirmEmail")
  public ResponseEntity<MessageDto> verifyEmail(@RequestParam("token") String token) {
    authService.verifyEmail(token);
    return ResponseEntity.accepted().build();
  }

  // # TODO: how to reach this endpoint?
  @GetMapping("/resendConfirmation")
  public ResponseEntity<MessageDto> resendConfirmationEmail(@RequestParam("email") String email) {
    authService.resendConfirmationEmail(email);
    return ResponseEntity.ok().build();
  }
}

