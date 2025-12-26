package com.example.demo.service.impl;

import com.example.demo.entity.BreachRule;
import com.example.demo.repository.BreachRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BreachRuleServiceImpl {
    private final BreachRuleRepository breachRuleRepository;

    public BreachRule createRule(BreachRule rule) {
        if (rule.getPenaltyPerDay().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Penalty per day must be positive");
        }
        if (rule.getMaxPenaltyPercentage() > 100.0 || rule.getMaxPenaltyPercentage() < 0) {
            throw new IllegalArgumentException("Max penalty percentage must be between 0-100");
        }
        return breachRuleRepository.save(rule);
    }

    public BreachRule getActiveDefaultOrFirst() {
        return breachRuleRepository.findFirstByActiveTrueOrderByIsDefaultRuleDesc()
            .orElseThrow(() -> new ResourceNotFoundException("No active breach rule found"));
    }

    public void deactivateRule(Long id) {
        BreachRule rule = breachRuleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Rule not found: " + id));
        rule.setActive(false);
        breachRuleRepository.save(rule);
    }

    public List<BreachRule> getAllRules() {
        return breachRuleRepository.findAll();
    }
}
