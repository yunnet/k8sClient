package com.dmakarov.config.advice;

import com.dmakarov.model.exception.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ResponseExceptionHandler {

  /**
   * Handles Response Exception, wrapping it with Response entity.
   *
   * @param e service exception
   * @return response entity
   */
  @ExceptionHandler
  public ResponseEntity handleException(ClientException e) {
    log.error(e.getMessage());
    return ResponseEntity
        .status(e.getHttpStatus())
        .body(e.getMessage());
  }

}
