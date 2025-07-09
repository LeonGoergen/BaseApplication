package com.baseproject.exception;

import lombok.NonNull;

public class ServiceFailedException extends BaseException {

  public ServiceFailedException(@NonNull ExceptionEnum exceptionEnum) {
    super(exceptionEnum);
  }
}
