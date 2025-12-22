package com.example.demo.controller;

import com.example.demo.entity.Contract;
import com.example.demo.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@Tag(name = "Contract Management", description = "APIs for managing contracts")
public class ContractController {
    
    private final ContractService contractService;
    
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }
    
    @GetMapping
    @Operation(summary = "Get all contracts", description = "Retrieve a list of all contracts")
    public ResponseEntity<List<Contract>> getAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        return ResponseEntity.ok(contracts);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get contract by ID", description = "Retrieve a specific contract by its ID")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        Contract contract = contractService.getContractById(id);
        return ResponseEntity.ok(contract);
    }
    
    @PostMapping
    @Operation(summary = "Create new contract", description = "Create a new contract")
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        Contract createdContract = contractService.createContract(contract);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContract);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update contract", description = "Update an existing contract")
    public ResponseEntity<Contract> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        Contract updatedContract = contractService.updateContract(id, contract);
        return ResponseEntity.ok(updatedContract);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete contract", description = "Delete a contract by its ID")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}