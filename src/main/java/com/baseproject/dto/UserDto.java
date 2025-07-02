package com.baseproject.dto;

import com.baseproject.model.enums.UserRoleEnum;
import lombok.NonNull;

import java.util.*;

public record UserDto(
    @NonNull UUID id,
    @NonNull String username,
    @NonNull String email,
    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull String phoneNumber,
    String address,
    String city,
    String country,
    String postalCode,
    @NonNull Boolean active,
    @NonNull Set<UserRoleEnum> roles
) {}
