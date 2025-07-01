package com.baseproject.exception;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class BaseException extends RuntimeException {

  @NonNull
  private final ExceptionEnum exceptionEnum;
  private String reference;
  private HttpStatus httpStatus;

  public String getCode()
  {
    return exceptionEnum.getCode();
  }

  @Override
  public String getMessage()
  {
    return exceptionEnum.getMessage();
  }
}
