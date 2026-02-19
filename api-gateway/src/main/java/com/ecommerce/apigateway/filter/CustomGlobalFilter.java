package com.ecommerce.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("Global Filter: request intercepted"+exchange.getRequest().getURI());
        return chain.filter(exchange).then(Mono.fromRunnable(()
                -> System.out.println("Global Filter: response sent")));
    }
}
