package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PenaltyCalculationServiceImpl {
    private final ContractRepository contractRepository;
    private final DeliveryRecordRepository deliveryRecordRepository;
    private final BreachRuleRepository breachRuleRepository;
    private final PenaltyCalculationRepository penaltyCalculationRepository;

    public PenaltyCalculation calculatePenalty(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));

        var latestDelivery = deliveryRecordRepository.findFirstByContractIdOrderByDeliveryDateDesc(contractId);
        if (latestDelivery.isEmpty()) {
            throw new ResourceNotFoundException("No delivery record found");
        }

        DeliveryRecord delivery = latestDelivery.get();
        BreachRule rule = breachRuleRepository.findFirstByActiveTrueOrderByIsDefaultRuleDesc()
            .orElseThrow(() -> new ResourceNotFoundException("No active breach rule"));

        long daysDelayed = Math.max(0, 
            java.time.temporal.ChronoUnit.DAYS.between(contract.getAgreedDeliveryDate(), delivery.getDeliveryDate()));

        BigDecimal penalty = rule.getPenaltyPerDay()
            .multiply(BigDecimal.valueOf(daysDelayed));
        
        BigDecimal maxPenalty = contract.getBaseContractValue()
            .multiply(BigDecimal.valueOf(rule.getMaxPenaltyPercentage() / 100.0));
        
        penalty = penalty.min(maxPenalty);

        PenaltyCalculation calc = PenaltyCalculation.builder()
            .contract(contract)
            .daysDelayed((int) daysDelayed)
            .calculatedPenalty(penalty)
            .build();

        return penaltyCalculationRepository.save(calc);
    }

    public PenaltyCalculation getCalculationById(Long id) {
        return penaltyCalculationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Calculation not found"));
    }

    public List<PenaltyCalculation> getCalculationsForContract(Long contractId) {
        return penaltyCalculationRepository.findByContractIdOrderByCalculatedAtDesc(contractId);
    }
}
