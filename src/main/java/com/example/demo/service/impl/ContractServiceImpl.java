package com.example.demo.service.impl;

import com.example.demo.entity.Contract;
import com.example.demo.entity.DeliveryRecord;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.DeliveryRecordRepository;
import com.example.demo.service.ContractService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {
    
    private ContractRepository contractRepository;
    private DeliveryRecordRepository deliveryRecordRepository;
    
    public ContractServiceImpl() {}
    
    public ContractServiceImpl(ContractRepository contractRepository, DeliveryRecordRepository deliveryRecordRepository) {
        this.contractRepository = contractRepository;
        this.deliveryRecordRepository = deliveryRecordRepository;
    }
    
    @Override
    public Contract createContract(Contract contract) {
        if (contract.getBaseContractValue() == null || contract.getBaseContractValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Base contract value must be greater than 0");
        }
        return contractRepository.save(contract);
    }
    
    @Override
    public Contract updateContract(Long id, Contract contract) {
        Contract existing = contractRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));
        
        if (contract.getTitle() != null) existing.setTitle(contract.getTitle());
        if (contract.getCounterpartyName() != null) existing.setCounterpartyName(contract.getCounterpartyName());
        if (contract.getAgreedDeliveryDate() != null) existing.setAgreedDeliveryDate(contract.getAgreedDeliveryDate());
        if (contract.getBaseContractValue() != null) existing.setBaseContractValue(contract.getBaseContractValue());
        
        return contractRepository.save(existing);
    }
    
    @Override
    public Contract getContractById(Long id) {
        return contractRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));
    }
    
    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }
    
    @Override
    public void updateContractStatus(Long contractId) {
        Contract contract = getContractById(contractId);
        Optional<DeliveryRecord> latestDelivery = deliveryRecordRepository.findFirstByContractIdOrderByDeliveryDateDesc(contractId);
        
        if (latestDelivery.isPresent()) {
            LocalDate deliveryDate = latestDelivery.get().getDeliveryDate();
            if (deliveryDate.isAfter(contract.getAgreedDeliveryDate())) {
                contract.setStatus("BREACHED");
            } else {
                contract.setStatus("COMPLETED");
            }
        } else {
            contract.setStatus("ACTIVE");
        }
        
        contractRepository.save(contract);
    }
}