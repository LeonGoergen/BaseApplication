package com.baseproject.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CategoryDto(
    UUID id,
    @NotBlank String name
) {}
