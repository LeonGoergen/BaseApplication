package com.baseproject.exception;

import com.baseproject.dto.ErrorResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.baseproject.exception.ExceptionEnum.UNKNOW_ERROR;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<Object> handleBaseException(BaseException ex) {

    ErrorResponseDto errorResponse = new ErrorResponseDto(
        LocalDateTime.now(),
        ex.getCode(),
        ex.getMessage(),
        ex.getReference()
    );

    HttpStatus httpStatus = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;

    log.error(errorResponse);

    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Object> handleValidationException(ValidationException ex) {

    ErrorResponseDto errorResponse = new ErrorResponseDto(
        LocalDateTime.now(),
        ex.getCode(),
        ex.getMessage(),
        ex.getReference()
    );

    log.error(errorResponse);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGeneralException(Exception ex) {

    ErrorResponseDto errorResponse = new ErrorResponseDto(
        LocalDateTime.now(),
        UNKNOW_ERROR.getCode(),
        UNKNOW_ERROR.getMessage()
    );

    log.error(errorResponse);

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Object> handleThrowable(Throwable ex) {
    ErrorResponseDto errorResponse = new ErrorResponseDto(
        LocalDateTime.now(),
        UNKNOW_ERROR.getCode(),
        UNKNOW_ERROR.getMessage()
    );

    log.error("Unhandled exception: ", ex);

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }


}
