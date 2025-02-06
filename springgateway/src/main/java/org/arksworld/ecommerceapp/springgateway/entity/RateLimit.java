package org.arksworld.ecommerceapp.springgateway.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RateLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String key;  // E.g., API key or User ID

    @Column(nullable = false)
    private Integer limitForPeriod;

    @Column(nullable = false)
    private Integer limitRefreshPeriod; // In seconds

    @Column(nullable = false)
    private Timestamp lastReset;

    @Column(nullable = false)
    private Integer currentRequests;

    // Getters and setters
}

