package com.example.demo.service.impl;

import com.example.demo.entity.BreachRule;
import com.example.demo.entity.Contract;
import com.example.demo.entity.DeliveryRecord;
import com.example.demo.entity.PenaltyCalculation;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BreachRuleRepository;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.DeliveryRecordRepository;
import com.example.demo.repository.PenaltyCalculationRepository;
import com.example.demo.service.PenaltyCalculationService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PenaltyCalculationServiceImpl implements PenaltyCalculationService {
    
    private ContractRepository contractRepository;
    private DeliveryRecordRepository deliveryRecordRepository;
    private BreachRuleRepository breachRuleRepository;
    private PenaltyCalculationRepository penaltyCalculationRepository;
    
    public PenaltyCalculationServiceImpl() {}
    
    public PenaltyCalculationServiceImpl(ContractRepository contractRepository, 
                                       DeliveryRecordRepository deliveryRecordRepository,
                                       BreachRuleRepository breachRuleRepository,
                                       PenaltyCalculationRepository penaltyCalculationRepository) {
        this.contractRepository = contractRepository;
        this.deliveryRecordRepository = deliveryRecordRepository;
        this.breachRuleRepository = breachRuleRepository;
        this.penaltyCalculationRepository = penaltyCalculationRepository;
    }
    
    @Override
    public PenaltyCalculation calculatePenalty(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));
        
        DeliveryRecord latestDelivery = deliveryRecordRepository.findFirstByContractIdOrderByDeliveryDateDesc(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("No delivery record"));
        
        BreachRule rule = breachRuleRepository.findFirstByActiveTrueOrderByIsDefaultRuleDesc()
            .orElseThrow(() -> new ResourceNotFoundException("No active breach rule"));
        
        LocalDate agreedDate = contract.getAgreedDeliveryDate();
        LocalDate actualDate = latestDelivery.getDeliveryDate();
        
        int daysDelayed = Math.max(0, (int) ChronoUnit.DAYS.between(agreedDate, actualDate));
        
        BigDecimal penaltyByDays = rule.getPenaltyPerDay().multiply(BigDecimal.valueOf(daysDelayed));
        BigDecimal maxPenalty = contract.getBaseContractValue()
            .multiply(BigDecimal.valueOf(rule.getMaxPenaltyPercentage()))
            .divide(BigDecimal.valueOf(100));
        
        BigDecimal finalPenalty = penaltyByDays.min(maxPenalty);
        
        PenaltyCalculation calculation = PenaltyCalculation.builder()
            .contract(contract)
            .daysDelayed(daysDelayed)
            .calculatedPenalty(finalPenalty)
            .appliedRule(rule)
            .build();
        
        return penaltyCalculationRepository.save(calculation);
    }
    
    @Override
    public PenaltyCalculation getCalculationById(Long id) {
        return penaltyCalculationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Calculation not found"));
    }
    
    @Override
    public List<PenaltyCalculation> getCalculationsForContract(Long contractId) {
        return penaltyCalculationRepository.findByContractId(contractId);
    }
}