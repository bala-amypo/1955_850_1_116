package com.example.demo.service.impl;

import com.example.demo.entity.BreachRule;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BreachRuleRepository;
import com.example.demo.service.BreachRuleService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BreachRuleServiceImpl implements BreachRuleService {
    
    private BreachRuleRepository breachRuleRepository;
    
    public BreachRuleServiceImpl() {}
    
    public BreachRuleServiceImpl(BreachRuleRepository breachRuleRepository) {
        this.breachRuleRepository = breachRuleRepository;
    }
    
    @Override
    public BreachRule createRule(BreachRule rule) {
        if (rule.getPenaltyPerDay() == null || rule.getPenaltyPerDay().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Penalty per day must be greater than 0");
        }
        if (rule.getMaxPenaltyPercentage() == null || rule.getMaxPenaltyPercentage() < 0 || rule.getMaxPenaltyPercentage() > 100) {
            throw new IllegalArgumentException("Max penalty percentage must be between 0 and 100");
        }
        return breachRuleRepository.save(rule);
    }
    
    @Override
    public BreachRule updateRule(Long id, BreachRule rule) {
        BreachRule existing = getRuleById(id);
        if (rule.getRuleName() != null) existing.setRuleName(rule.getRuleName());
        if (rule.getPenaltyPerDay() != null) existing.setPenaltyPerDay(rule.getPenaltyPerDay());
        if (rule.getMaxPenaltyPercentage() != null) existing.setMaxPenaltyPercentage(rule.getMaxPenaltyPercentage());
        if (rule.getActive() != null) existing.setActive(rule.getActive());
        if (rule.getIsDefaultRule() != null) existing.setIsDefaultRule(rule.getIsDefaultRule());
        return breachRuleRepository.save(existing);
    }
    
    @Override
    public List<BreachRule> getAllRules() {
        return breachRuleRepository.findAll();
    }
    
    @Override
    public BreachRule getRuleById(Long id) {
        return breachRuleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));
    }
    
    @Override
    public void deactivateRule(Long id) {
        BreachRule rule = getRuleById(id);
        rule.setActive(false);
        breachRuleRepository.save(rule);
    }
    
    @Override
    public BreachRule getActiveDefaultOrFirst() {
        return breachRuleRepository.findFirstByActiveTrueOrderByIsDefaultRuleDesc()
            .orElseThrow(() -> new ResourceNotFoundException("No active breach rule"));
    }
}