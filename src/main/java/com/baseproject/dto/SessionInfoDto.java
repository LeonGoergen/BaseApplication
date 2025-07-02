package com.baseproject.dto;

import java.time.LocalDateTime;

public record SessionInfoDto(
    String id,
    LocalDateTime creationTime,
    LocalDateTime lastAccessedTime,
    Integer maxInactiveIntervalSeconds,
    LocalDateTime expiresAt
) { }
