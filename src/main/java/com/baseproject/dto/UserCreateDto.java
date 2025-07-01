package com.baseproject.dto;

import lombok.NonNull;

public record UserCreateDto(
    @NonNull String username,
    @NonNull String password,
    @NonNull String email,
    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull String phoneNumber,
    String address,
    String city,
    String country,
    String postalCode
) { }
