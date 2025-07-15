package com.baseproject.exception.exceptions;

import com.baseproject.exception.ExceptionEnum;
import lombok.NonNull;

public class RessourceNotFoundException extends BaseException
{
  public RessourceNotFoundException(@NonNull ExceptionEnum exceptionEnum) {
    super(exceptionEnum);
  }
}
