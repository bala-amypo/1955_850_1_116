package com.example.demo.controller;

import com.example.demo.entity.PenaltyCalculation;
import com.example.demo.service.PenaltyCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/penalties")
public class PenaltyCalculationController {
    
    @Autowired
    private PenaltyCalculationService penaltyCalculationService;
    
    @PostMapping("/calculate/{contractId}")
    public ResponseEntity<PenaltyCalculation> calculatePenalty(@PathVariable Long contractId) {
        return ResponseEntity.ok(penaltyCalculationService.calculatePenalty(contractId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PenaltyCalculation> getCalculation(@PathVariable Long id) {
        return ResponseEntity.ok(penaltyCalculationService.getCalculationById(id));
    }
    
    @GetMapping("/contract/{contractId}")
    public ResponseEntity<List<PenaltyCalculation>> getCalculationsForContract(@PathVariable Long contractId) {
        return ResponseEntity.ok(penaltyCalculationService.getCalculationsForContract(contractId));
    }
}