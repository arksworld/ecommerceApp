package org.arksworld.ecommerceapp.springgateway.repository;

import org.arksworld.ecommerceapp.springgateway.entity.RateLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface RateLimitRepository extends JpaRepository<RateLimit, Long> {

    Optional<RateLimit> findByKey(String key);

    @Modifying
    @Query("UPDATE RateLimit r SET r.currentRequests = :currentRequests WHERE r.key = :key")
    void updateCurrentRequests(@Param("key") String key, @Param("currentRequests") int currentRequests);

    @Modifying
    @Query("UPDATE RateLimit r SET r.lastReset = :lastReset WHERE r.key = :key")
    void updateLastReset(@Param("key") String key, @Param("lastReset") Timestamp lastReset);
}

