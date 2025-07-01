package com.baseproject.dto;

import lombok.*;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    @NonNull LocalDateTime timestamp,
    @NonNull String code,
    @NonNull String message,
    String reference
) {
  public ErrorResponseDto(@NonNull LocalDateTime timestamp, @NonNull String code, @NonNull String message) {
    this(timestamp, code, message, null);
  }
}
