package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.BreachReportRepository;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.PenaltyCalculationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BreachReportServiceImpl {
    private final ContractRepository contractRepository;
    private final PenaltyCalculationRepository penaltyCalculationRepository;
    private final BreachReportRepository breachReportRepository;

    public BreachReport generateReport(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));

        var latestCalc = penaltyCalculationRepository.findTopByContractIdOrderByCalculatedAtDesc(contractId);
        if (latestCalc.isEmpty()) {
            throw new ResourceNotFoundException("No penalty calculation found");
        }

        PenaltyCalculation calc = latestCalc.get();
        BreachReport report = BreachReport.builder()
            .contract(contract)
            .daysDelayed(calc.getDaysDelayed())
            .penaltyAmount(calc.getCalculatedPenalty())
            .build();

        return breachReportRepository.save(report);
    }

    public List<BreachReport> getReportsForContract(Long contractId) {
        return breachReportRepository.findByContractIdOrderByReportedAtDesc(contractId);
    }

    public List<BreachReport> getAllReports() {
        return breachReportRepository.findAll();
    }
}
