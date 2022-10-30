package com.huemap.backend.common.presentation;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.huemap.backend.common.response.error.ErrorCode;
import com.huemap.backend.common.response.error.ErrorResponse;

@RestControllerAdvice
public class ControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE,
                                                    e.getBindingResult());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConversionFailedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> conversionFailedException(
      ConversionFailedException e) {
    final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }


}
