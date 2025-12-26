package com.example.demo.controller;

import com.example.demo.entity.BreachReport;
import com.example.demo.service.BreachReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class BreachReportController {
    
    @Autowired
    private BreachReportService breachReportService;
    
    @PostMapping("/generate/{contractId}")
    public ResponseEntity<BreachReport> generateReport(@PathVariable Long contractId) {
        return ResponseEntity.ok(breachReportService.generateReport(contractId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BreachReport> getReport(@PathVariable Long id) {
        return ResponseEntity.ok(breachReportService.getReportById(id));
    }
    
    @GetMapping("/contract/{contractId}")
    public ResponseEntity<List<BreachReport>> getReportsForContract(@PathVariable Long contractId) {
        return ResponseEntity.ok(breachReportService.getReportsForContract(contractId));
    }
    
    @GetMapping
    public ResponseEntity<List<BreachReport>> getAllReports() {
        return ResponseEntity.ok(breachReportService.getAllReports());
    }
}