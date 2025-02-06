package org.arksworld.ecommerceapp.springgateway;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

import org.arksworld.ecommerceapp.springgateway.web.Resilience4jRateLimiterFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        return RateLimiterRegistry.ofDefaults();
    }


    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, Resilience4jRateLimiterFilter rateLimiterFilter) {
        return builder.routes()
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f.filter( rateLimiterFilter))
                        .uri("lb://USERSERVICE"))
                .build();
    }

    @Bean
    public RateLimiter userServiceRateLimiter(RateLimiterRegistry rateLimiterRegistry) {
        return rateLimiterRegistry.rateLimiter("user-service-rate-limiter", RateLimiterConfig.custom()
                .limitForPeriod(5) // Allow max 5 requests
                .limitRefreshPeriod(Duration.ofSeconds(10)) // Refresh limit every 10 seconds
                .timeoutDuration(Duration.ofMillis(500)) // Wait time for new request
                .build());
    }

}
