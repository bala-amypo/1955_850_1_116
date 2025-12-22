package com.example.demo.service.impl;

import com.example.demo.entity.Contract;
import com.example.demo.repository.ContractRepository;
import com.example.demo.service.ContractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ContractServiceImpl implements ContractService {
    
    private final ContractRepository contractRepository;
    
    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }
    
    @Override
    public Contract createContract(Contract contract) {
        return contractRepository.save(contract);
    }
    
    @Override
    public Contract getContractById(Long id) {
        return contractRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));
    }
    
    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }
    
    @Override
    public Contract updateContract(Long id, Contract contract) {
        Contract existing = getContractById(id);
        existing.setTitle(contract.getTitle());
        existing.setCounterpartyName(contract.getCounterpartyName());
        existing.setAgreedDeliveryDate(contract.getAgreedDeliveryDate());
        existing.setBaseContractValue(contract.getBaseContractValue());
        existing.setStatus(contract.getStatus());
        return contractRepository.save(existing);
    }
    
    @Override
    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }
}