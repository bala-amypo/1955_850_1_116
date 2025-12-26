package com.example.demo.service.impl;

import com.example.demo.entity.BreachReport;
import com.example.demo.entity.Contract;
import com.example.demo.entity.PenaltyCalculation;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BreachReportRepository;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.PenaltyCalculationRepository;
import com.example.demo.service.BreachReportService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BreachReportServiceImpl implements BreachReportService {
    
    private BreachReportRepository breachReportRepository;
    private PenaltyCalculationRepository penaltyCalculationRepository;
    private ContractRepository contractRepository;
    
    public BreachReportServiceImpl() {}
    
    public BreachReportServiceImpl(BreachReportRepository breachReportRepository,
                                 PenaltyCalculationRepository penaltyCalculationRepository,
                                 ContractRepository contractRepository) {
        this.breachReportRepository = breachReportRepository;
        this.penaltyCalculationRepository = penaltyCalculationRepository;
        this.contractRepository = contractRepository;
    }
    
    @Override
    public BreachReport generateReport(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));
        
        PenaltyCalculation calculation = penaltyCalculationRepository.findTopByContractIdOrderByCalculatedAtDesc(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("No penalty calculation"));
        
        BreachReport report = BreachReport.builder()
            .contract(contract)
            .daysDelayed(calculation.getDaysDelayed())
            .penaltyAmount(calculation.getCalculatedPenalty())
            .remarks("Generated from penalty calculation")
            .build();
        
        return breachReportRepository.save(report);
    }
    
    @Override
    public BreachReport getReportById(Long id) {
        return breachReportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
    }
    
    @Override
    public List<BreachReport> getReportsForContract(Long contractId) {
        return breachReportRepository.findByContractId(contractId);
    }
    
    @Override
    public List<BreachReport> getAllReports() {
        return breachReportRepository.findAll();
    }
}