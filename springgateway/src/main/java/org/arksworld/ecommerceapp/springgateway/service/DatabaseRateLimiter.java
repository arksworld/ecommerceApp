package org.arksworld.ecommerceapp.springgateway.service;

import org.arksworld.ecommerceapp.springgateway.entity.RateLimit;
import org.arksworld.ecommerceapp.springgateway.repository.RateLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class DatabaseRateLimiter {

    @Autowired
    private RateLimitRepository rateLimitRepository;

    private final int LIMIT_FOR_PERIOD = 5;  // Max 5 requests
    private final int REFRESH_PERIOD_SECONDS = 10;  // Refresh every 10 seconds

    public boolean isRateLimited(String key) {
        Optional<RateLimit> rateLimitOpt = rateLimitRepository.findByKey(key);

        if (rateLimitOpt.isEmpty()) {
            // Initialize rate limit data if not found
            RateLimit rateLimit = new RateLimit();
            rateLimit.setKey(key);
            rateLimit.setLimitForPeriod(LIMIT_FOR_PERIOD);
            rateLimit.setLimitRefreshPeriod(REFRESH_PERIOD_SECONDS);
            rateLimit.setLastReset(new Timestamp(System.currentTimeMillis()));
            rateLimit.setCurrentRequests(0);

            rateLimitRepository.save(rateLimit);
            return false;  // Allow the first request
        }

        RateLimit rateLimit = rateLimitOpt.get();

        // Check if the rate limit period has expired
        long elapsed = System.currentTimeMillis() - rateLimit.getLastReset().getTime();
        if (elapsed > TimeUnit.SECONDS.toMillis(REFRESH_PERIOD_SECONDS)) {
            // Reset the limit
            rateLimit.setLastReset(new Timestamp(System.currentTimeMillis()));
            rateLimit.setCurrentRequests(0);
            rateLimitRepository.updateLastReset(key, rateLimit.getLastReset());
        }

        // Check if requests exceed the limit
        if (rateLimit.getCurrentRequests() >= rateLimit.getLimitForPeriod()) {
            return true;  // Rate limit exceeded
        }

        // Increment the current requests count
        rateLimit.setCurrentRequests(rateLimit.getCurrentRequests() + 1);
        rateLimitRepository.updateCurrentRequests(key, rateLimit.getCurrentRequests());

        return false;  // Allow the request
    }
}

