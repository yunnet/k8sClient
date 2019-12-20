package com.dmakarov.config.advice;

import com.dmakarov.model.exception.ClientException;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ResponseExceptionHandler {

  /**
   * Handles ClientException, wrapping it with Response entity.
   *
   * @param e service exception
   * @return response entity
   */
  @ExceptionHandler
  public ResponseEntity handleException(ClientException e) {
    log.error(e.getMessage());
    return ResponseEntity
        .status(e.getHttpStatus())
        .body(ResponseBody.builder()
            .status(e.getHttpStatus())
            .message(e.getMessage())
            .build()
        );
  }

  @Value
  @Builder
  private static class ResponseBody {
    HttpStatus status;
    String message;
  }

}
