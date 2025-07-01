package com.baseproject.exception;

import lombok.*;

public class ValidationException extends BaseException {

  public ValidationException(@NonNull ExceptionEnum exceptionEnum) {
    super(exceptionEnum);
  }
}
