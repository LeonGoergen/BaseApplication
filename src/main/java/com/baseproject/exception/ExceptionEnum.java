package com.baseproject.exception;

import lombok.*;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {

  TOO_MANY_REQUESTS("E001", "Too many requests"),

  USER_NOT_FOUND("E002", "User not found"),

  USERNAME_ALREADY_EXISTS("E003", "Username already exists"),
  EMAIL_ALREADY_EXISTS("E004", "Email already exists"),

  UNKNOW_ERROR("E999", "Unexpected error occurred");

  private final String code;
  private final String message;
}
