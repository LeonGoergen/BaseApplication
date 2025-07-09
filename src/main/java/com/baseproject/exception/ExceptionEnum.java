package com.baseproject.exception;

import lombok.*;

@Getter
@AllArgsConstructor
public enum ExceptionEnum
{

  TOO_MANY_REQUESTS("E001", "Too many requests"),

  INVALID_CREDENTIALS("E000", "Invalid credentials"),
  UNAUTHORIZED("E002", "Unauthorized"),
  USER_NOT_FOUND("E002", "User not found"),
  USER_NOT_VERIFIED("E003", "User not verified"),
  USER_ALREADY_VERIFIED("E003", "User already verified"),
  SESSION_NOT_FOUND("E005", "Session not found"),
  EMAIL_VERIFICATION_TOKEN_NOT_FOUND("E006", "Email verification token not found"),
  EMAIL_VERIFICATION_TOKEN_EXPIRED("E007", "Email verification token expired"),

  EMAIL_ALREADY_EXISTS("E004", "Email already exists"),

  MAIL_SERVICE_NOT_AVAILABLE("E005", "Mail service is not available"),

  UNKNOW_ERROR("E999", "Unexpected error occurred");

  private final String code;
  private final String message;
}
