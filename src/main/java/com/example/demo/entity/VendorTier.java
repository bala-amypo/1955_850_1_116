package com.example.demo.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "vendor_tiers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String tierName;
    private Integer minScoreThreshold;
    private Boolean active;
}