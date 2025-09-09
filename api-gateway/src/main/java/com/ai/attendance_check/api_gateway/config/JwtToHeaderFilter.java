package com.ai.attendance_check.api_gateway.config;

import org.springframework.core.Ordered;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Component
public class JwtToHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> {
                    if (auth instanceof JwtAuthenticationToken) {
                        Jwt jwt = (Jwt) ((JwtAuthenticationToken) auth).getToken();
                        String userId = jwt.getClaimAsString("sub"); // or "preferred_username"
                        ServerHttpRequest mutated = exchange.getRequest().mutate()
                                .header("X-User-ID", userId)
                                .build();
                        return chain.filter(exchange.mutate().request(mutated).build());
                    } else {
                        return chain.filter(exchange);
                    }
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
