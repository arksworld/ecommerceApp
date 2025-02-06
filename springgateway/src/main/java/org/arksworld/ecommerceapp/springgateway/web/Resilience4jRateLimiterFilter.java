package org.arksworld.ecommerceapp.springgateway.web;


import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.arksworld.ecommerceapp.springgateway.service.DatabaseRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class Resilience4jRateLimiterFilter implements GatewayFilter, Ordered {


    @Autowired
    private DatabaseRateLimiter databaseRateLimiter;

    private final RateLimiter rateLimiter;

    @Autowired
    public Resilience4jRateLimiterFilter(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiter = rateLimiterRegistry.rateLimiter("user-service-rate-limiter");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!rateLimiter.acquirePermission()) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
