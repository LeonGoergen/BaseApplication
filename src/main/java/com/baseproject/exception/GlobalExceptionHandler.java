package com.baseproject.exception;

import com.baseproject.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.*;

import static com.baseproject.exception.ExceptionEnum.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<Object> handleBaseException(BaseException ex) {

    ErrorResponseDto errorResponse = new  ErrorResponseDto();
    errorResponse.setTimestamp(OffsetDateTime.now());
    errorResponse.setCode(ex.getCode());
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setReference(ex.getReference());

    HttpStatus httpStatus = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;

    logMessage(errorResponse, ex);

    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Object> handleValidationException(ValidationException ex) {

    ErrorResponseDto errorResponse = new  ErrorResponseDto();
    errorResponse.setTimestamp(OffsetDateTime.now());
    errorResponse.setCode(ex.getCode());
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setReference(ex.getReference());
    logMessage(errorResponse, ex);

    HttpStatus httpStatus = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.BAD_REQUEST;

    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  @ExceptionHandler(ServiceFailedException.class)
  public ResponseEntity<Object> handleServiceFailedException(ServiceFailedException ex) {

    ErrorResponseDto errorResponse = new  ErrorResponseDto();
    errorResponse.setTimestamp(OffsetDateTime.now());
    errorResponse.setCode(ex.getCode());
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setReference(ex.getReference());
    logMessage(errorResponse, ex);

    HttpStatus httpStatus = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;

    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex) {
    ErrorResponseDto errorResponse = new  ErrorResponseDto();
    errorResponse.setTimestamp(OffsetDateTime.now());
    errorResponse.setCode(RESOURCE_NOT_FOUND.getCode());
    errorResponse.setMessage(RESOURCE_NOT_FOUND.getMessage());
    errorResponse.setReference(ex.getResourcePath());

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGeneralException(Exception ex) {

    ErrorResponseDto errorResponse = new  ErrorResponseDto();
    errorResponse.setTimestamp(OffsetDateTime.now());
    errorResponse.setCode(UNKNOW_ERROR.getCode());
    errorResponse.setMessage(UNKNOW_ERROR.getMessage());
    logMessage(errorResponse, ex);

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Object> handleThrowable(Throwable ex) {
    ErrorResponseDto errorResponse = new  ErrorResponseDto();
    errorResponse.setTimestamp(OffsetDateTime.now());
    errorResponse.setCode(UNKNOW_ERROR.getCode());
    errorResponse.setMessage(UNKNOW_ERROR.getMessage());
    logMessage(errorResponse, ex);

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private void logMessage(ErrorResponseDto errorResponse, Exception ex) {
    log.error(
        "Exception occurred [timestamp: {}, code: {}, message: {}, reference: {}]",
        errorResponse.getTimestamp(),
        errorResponse.getCode(),
        errorResponse.getMessage(),
        errorResponse.getReference()
    );
    log.error(ex.getMessage(), ex);
  }

  private void logMessage(ErrorResponseDto errorResponse, Throwable ex) {
    log.error(
        "Exception occurred [timestamp: {}, code: {}, message: {}, reference: {}]",
        errorResponse.getTimestamp(),
        errorResponse.getCode(),
        errorResponse.getMessage(),
        errorResponse.getReference()
    );
    log.error(ex.getMessage(), ex);
  }
}
