package com.tuanemtramtinh.itscommon.exception;

import com.tuanemtramtinh.itscommon.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException e) {
    log.error("RuntimeException: {}", e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.error(e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
    log.error("Exception: {}", e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error("Internal server error: " + e.getMessage()));
  }
}
