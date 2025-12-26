package com.example.demo.entity;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "penalty_calculations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;
    
    private Integer daysDelayed;
    private BigDecimal calculatedPenalty;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applied_rule_id")
    private BreachRule appliedRule;
    
    @Column(updatable = false)
    private LocalDateTime calculatedAt;
    
    @PrePersist
    protected void onCreate() {
        calculatedAt = LocalDateTime.now();
    }
}