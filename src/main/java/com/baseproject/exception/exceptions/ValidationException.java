package com.baseproject.exception.exceptions;

import com.baseproject.exception.ExceptionEnum;
import lombok.*;

public class ValidationException extends BaseException
{

  public ValidationException(@NonNull ExceptionEnum exceptionEnum) {
    super(exceptionEnum);
  }
}
