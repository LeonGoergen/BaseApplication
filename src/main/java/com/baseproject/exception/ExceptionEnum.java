package com.baseproject.exception;

import lombok.*;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {

  TOO_MANY_REQUESTS("E001", "Too many requests"),
  UNKNOW_ERROR("E999", "Unexpected error occurred");

  private final String code;
  private final String message;
}
