package com.baseproject.exception;

import com.baseproject.dto.*;
import com.baseproject.exception.exceptions.*;
import com.baseproject.model.ExceptionLog;
import com.baseproject.repository.ExceptionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.*;

import static com.baseproject.exception.ExceptionEnum.*;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ExceptionLogRepository exceptionLogRepository;

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<Object> handleBaseException(BaseException ex) {

    ErrorResponseDto errorResponse = new ErrorResponseDto();
    errorResponse.setTimestamp(OffsetDateTime.now());
    errorResponse.setCode(ex.getCode());
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setReference(ex.getReference());

    logException(errorResponse);

    HttpStatus httpStatus = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;

    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Object> handleValidationException(ValidationException ex) {

    ErrorResponseDto errorResponse = new ErrorResponseDto();
    errorResponse.setTimestamp(OffsetDateTime.now());
    errorResponse.setCode(ex.getCode());
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setReference(ex.getReference());

    logException(errorResponse);

    HttpStatus httpStatus = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.BAD_REQUEST;

    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  @ExceptionHandler(ServiceFailedException.class)
  public ResponseEntity<Object> handleServiceFailedException(ServiceFailedException ex) {

    ErrorResponseDto errorResponse = new ErrorResponseDto();
    errorResponse.setTimestamp(OffsetDateTime.now());
    errorResponse.setCode(ex.getCode());
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setReference(ex.getReference());

    logException(errorResponse);

    HttpStatus httpStatus = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;

    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex) {
    ErrorResponseDto errorResponse = new ErrorResponseDto();
    errorResponse.setTimestamp(OffsetDateTime.now());
    errorResponse.setCode(RESOURCE_NOT_FOUND.getCode());
    errorResponse.setMessage(RESOURCE_NOT_FOUND.getMessage());
    errorResponse.setReference(ex.getResourcePath());

    logException(errorResponse);;

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGeneralException(Exception ex) {
    ErrorResponseDto errorResponse = new ErrorResponseDto();
    errorResponse.setTimestamp(OffsetDateTime.now());
    errorResponse.setCode(UNKNOW_ERROR.getCode());
    errorResponse.setMessage(UNKNOW_ERROR.getMessage());

    log.error(ex.getMessage(), ex);

    ExceptionLog exceptionLog = new ExceptionLog();
    exceptionLog.setTimestamp(OffsetDateTime.now());
    exceptionLog.setExceptionType(ex.getClass().getName());
    exceptionLog.setMessage(ex.getMessage());
    exceptionLog.setMethod(ex.getStackTrace()[0].toString());
    exceptionLog.setRead(false);
    exceptionLog.setHandled(false);

    exceptionLogRepository.save(exceptionLog);

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private void logException(ErrorResponseDto errorResponse) {
    StringBuilder sb = new StringBuilder();
    sb.append("Exception occurred: ")
      .append("Code: ").append(errorResponse.getCode())
      .append(", Message: ").append(errorResponse.getMessage());

    if (errorResponse.getReference() != null) {
      sb.append(", Reference: ").append(errorResponse.getReference());
    }

    log.warn(sb.toString());
  }
}
