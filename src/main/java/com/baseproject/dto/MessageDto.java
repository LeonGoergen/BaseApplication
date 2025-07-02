package com.baseproject.dto;

import java.time.LocalDateTime;

public record MessageDto(
    LocalDateTime timestamp,
    String message
) { }
