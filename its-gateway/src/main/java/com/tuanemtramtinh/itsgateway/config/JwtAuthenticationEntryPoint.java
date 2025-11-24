package com.tuanemtramtinh.itsgateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuanemtramtinh.itscommon.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // Log thông tin về lỗi authentication
    String path = exchange.getRequest().getPath().value();
    String method = exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name() : "UNKNOWN";
    String message = ex.getMessage();
    String errorMessage = "Token không hợp lệ hoặc đã hết hạn. Vui lòng đăng nhập lại.";

    // Kiểm tra xem có phải token hết hạn không
    if (message != null && (message.toLowerCase().contains("expired") ||
        message.contains("Jwt expired") ||
        message.contains("expired at"))) {
      log.warn("JWT token expired - Method: {}, Path: {}, Error: {}", method, path, message);
      errorMessage = "Token đã hết hạn. Vui lòng đăng nhập lại.";
    } else if (message != null && message.contains("Invalid")) {
      log.warn("JWT token invalid - Method: {}, Path: {}, Error: {}", method, path, message);
      errorMessage = "Token không hợp lệ. Vui lòng đăng nhập lại.";
    } else {
      log.warn("JWT authentication failed - Method: {}, Path: {}, Error: {}", method, path, message);
    }

    // Tạo response body
    ApiResponse<Object> apiResponse = ApiResponse.error(errorMessage);

    try {
      String jsonResponse = objectMapper.writeValueAsString(apiResponse);
      byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
      DataBuffer buffer = response.bufferFactory().wrap(bytes);
      return response.writeWith(Mono.just(buffer));
    } catch (JsonProcessingException e) {
      log.error("Error serializing error response", e);
      byte[] bytes = "{\"success\":false,\"message\":\"Internal server error\"}".getBytes(StandardCharsets.UTF_8);
      DataBuffer buffer = response.bufferFactory().wrap(bytes);
      return response.writeWith(Mono.just(buffer));
    }
  }
}
