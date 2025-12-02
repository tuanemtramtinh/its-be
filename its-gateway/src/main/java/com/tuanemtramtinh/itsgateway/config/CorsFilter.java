package com.tuanemtramtinh.itsgateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE) // Quan trọng: Chạy trước tất cả các filter khác
public class CorsFilter implements WebFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    ServerHttpResponse response = exchange.getResponse();
    HttpHeaders headers = response.getHeaders();

    // Lấy Origin từ request gửi lên
    String origin = request.getHeaders().getOrigin();

    // 1. Cấu hình CORS Header
    // Nếu có Origin thì trả về đúng Origin đó (để hỗ trợ credentials/cookies), nếu
    // không thì *
    headers.add("Access-Control-Allow-Origin", origin != null ? origin : "*");
    headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS, HEAD, PATCH");
    headers.add("Access-Control-Allow-Headers",
        "Origin, X-Requested-With, Content-Type, Accept, Authorization, Cache-Control");
    headers.add("Access-Control-Allow-Credentials", "true");
    headers.add("Access-Control-Max-Age", "3600");

    // 2. Nếu là request OPTIONS (Preflight check) -> Trả về OK ngay lập tức
    if (request.getMethod() == HttpMethod.OPTIONS) {
      response.setStatusCode(HttpStatus.OK);
      return Mono.empty(); // Kết thúc request tại đây, không cho đi vào Security
    }

    // 3. Nếu không phải OPTIONS -> Cho đi tiếp vào các filter sau (Security...)
    return chain.filter(exchange);
  }
}