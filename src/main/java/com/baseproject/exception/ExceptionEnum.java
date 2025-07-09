package com.baseproject.exception;

import lombok.*;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {

  TOO_MANY_REQUESTS("E001", "Too many requests"),

  INVALID_CREDENTIALS("E000", "Invalid credentials"),
  UNAUTHORIZED("E002", "Unauthorized"),
  USER_NOT_FOUND("E002", "User not found"),
  SESSION_NOT_FOUND("E005", "Session not found"),

  USERNAME_ALREADY_EXISTS("E003", "Username already exists"),
  EMAIL_ALREADY_EXISTS("E004", "Email already exists"),

  MAIL_SERVICE_NOT_AVAILABLE("E005", "Mail service is not available"),

  UNKNOW_ERROR("E999", "Unexpected error occurred");

  private final String code;
  private final String message;
}
