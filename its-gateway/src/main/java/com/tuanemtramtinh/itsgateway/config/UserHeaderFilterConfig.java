package com.tuanemtramtinh.itsgateway.config;

import java.util.stream.Collectors;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration
public class UserHeaderFilterConfig {

  @Bean
  public GlobalFilter userInfoHeaderFilter() {
    return (exchange, chain) -> exchange.getPrincipal()
        .cast(JwtAuthenticationToken.class)
        .flatMap(auth -> mutateRequestWithUserHeaders(exchange, auth)
            .flatMap(chain::filter))
        .switchIfEmpty(chain.filter(exchange));
  }

  private Mono<ServerWebExchange> mutateRequestWithUserHeaders(
      ServerWebExchange exchange,
      JwtAuthenticationToken auth) {
    var jwt = auth.getToken();

    String userId = jwt.getSubject();
    String email = jwt.getClaimAsString("email");
    String roles = auth.getAuthorities().stream()
        .map(a -> a.getAuthority())
        .collect(Collectors.joining(","));

    var mutatedRequest = exchange.getRequest().mutate()
        .header("X-User-Id", userId)
        .header("X-User-Email", email != null ? email : "")
        .header("X-User-Roles", roles)
        .build();

    return Mono.just(exchange.mutate().request(mutatedRequest).build());
  }
}