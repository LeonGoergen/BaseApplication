package com.baseproject.exception;

import lombok.NonNull;

public class RessourceNotFoundException extends BaseException
{
  public RessourceNotFoundException(@NonNull ExceptionEnum exceptionEnum) {
    super(exceptionEnum);
  }
}
