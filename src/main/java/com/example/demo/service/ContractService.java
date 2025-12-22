package com.example.demo.service;

import com.example.demo.entity.Contract;
import java.util.List;

public interface ContractService {
    Contract createContract(Contract contract);
    Contract getContractById(Long id);
    List<Contract> getAllContracts();
    Contract updateContract(Long id, Contract contract);
    void deleteContract(Long id);
}