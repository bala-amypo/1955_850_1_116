package com.example.demo.controller;

import com.example.demo.entity.Contract;
import com.example.demo.service.impl.ContractServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractServiceImpl contractService;

    public ContractController(ContractServiceImpl contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public List<Contract> getAll() {
        return contractService.getAllContracts();
    }
}
