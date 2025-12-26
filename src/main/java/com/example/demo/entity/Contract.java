package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contractNumber;
    private String title;
    private String counterpartyName;
    private LocalDate agreedDeliveryDate;
    private BigDecimal baseContractValue;

    @Enumerated(EnumType.STRING)
    private ContractStatus status = ContractStatus.ACTIVE;

    private LocalDate createdAt = LocalDate.now();
}
