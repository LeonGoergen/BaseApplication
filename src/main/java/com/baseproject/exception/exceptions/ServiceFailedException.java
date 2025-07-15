package com.baseproject.exception.exceptions;

import com.baseproject.exception.ExceptionEnum;
import lombok.NonNull;

public class ServiceFailedException extends BaseException
{

  public ServiceFailedException(@NonNull ExceptionEnum exceptionEnum) {
    super(exceptionEnum);
  }
}
