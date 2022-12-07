package com.huemap.backend.common.response.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import lombok.Getter;

@Getter
public class ErrorResponse {

  private int status;
  private String message;
  private List<FieldError> errors;

  private ErrorResponse(ErrorCode code, List<FieldError> errors) {
    this.message = code.getMessage();
    this.status = code.getStatus();
    this.errors = errors;
  }

  private ErrorResponse(ErrorCode code) {
    this.message = code.getMessage();
    this.status = code.getStatus();
  }

  public ErrorResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }

  public static ErrorResponse of(ErrorCode code, BindingResult bindingResult) {
    return new ErrorResponse(code, FieldError.of(bindingResult));
  }

  public static ErrorResponse of(ErrorCode code) {
    return new ErrorResponse(code);
  }

  public static ErrorResponse of(int status, String message) {
    return new ErrorResponse(status, message);
  }

  public static class FieldError {

    private String field;
    private String value;
    private String reason;

    public FieldError(String field, String value, String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }

    public String getField() {
      return field;
    }

    public String getValue() {
      return value;
    }

    public String getReason() {
      return reason;
    }

    private static List<FieldError> of(BindingResult bindingResult) {
      List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
      return fieldErrors.stream()
                        .map(error -> new FieldError(error.getField(),
                                                     error.getRejectedValue() == null ? "" :
                                                         error.getRejectedValue().toString(),
                                                     error.getDefaultMessage()))
                        .collect(Collectors.toList());
    }
  }
}
