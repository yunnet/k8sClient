package com.dmakarov.model.exception;

import org.springframework.http.HttpStatus;

public class ClientException extends RuntimeException {
  private final HttpStatus httpStatus;
  private final String message;

  /**
   * Constructor with params.
   */
  public ClientException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.message = message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
