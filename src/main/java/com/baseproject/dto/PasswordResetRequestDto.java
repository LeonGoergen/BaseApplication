package com.baseproject.dto;

public record PasswordResetRequestDto(
    String token,
    String password
) { }
