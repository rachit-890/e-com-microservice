package com.ecommerce.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class JwtGatewayFilter implements GatewayFilter, Ordered {

    @Autowired
    private JwtUtils jwtUtils;
    private static final List<String> PUBLIC_URLS = List.of("/api/users/register", "/api/users/login");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
           String path= exchange.getRequest().getURI().getPath();
           String method=exchange.getRequest().getMethod().name();
           if(PUBLIC_URLS.stream().anyMatch(path::contains)) {
               return chain.filter(exchange);
           }
           String authHeader=exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
           if(authHeader==null || !authHeader.startsWith("Bearer ") ){
               return unauthorized(exchange);
           }
           String token=authHeader.substring(7);
           if(!jwtUtils.validateToken(token)){
               return unauthorized(exchange);
           }
           String role=jwtUtils.getRole(token);
           if(!isAuthorized(role,path,method)){
               return forbidden(exchange);

           }
           String userId=jwtUtils.getUserId(token);
           ServerHttpRequest modifiedRequest=exchange.getRequest().mutate().header("X-User-Id",userId).header("X-User-Role",role).build();
           return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private static final Map<String,Map<String,List<String>>> ROLE_API_PERMISSION=Map.of(
            "ADMIN",Map.of("GET",List.of("/api/products"),
                    "POST",List.of("/api/products"),
                    "PUT",List.of("/api/products"),
                    "DELETE",List.of("/api/products")),
            "USER",Map.of("GET",List.of("/api/products")));

    private boolean isAuthorized(String role, String path, String method) {
        Map<String, List<String>> permission = ROLE_API_PERMISSION.get(role);
        if (permission == null) return false;

        List<String> allowedPaths = permission.get(method);
        if (allowedPaths == null) return false;

        return allowedPaths.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> forbidden(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }
}
