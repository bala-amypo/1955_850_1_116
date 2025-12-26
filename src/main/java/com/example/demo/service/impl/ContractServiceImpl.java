package com.example.demo.service.impl;

import com.example.demo.entity.Contract;
import com.example.demo.entity.ContractStatus;
import com.example.demo.entity.DeliveryRecord;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.DeliveryRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl {
    private final ContractRepository contractRepository;
    private final DeliveryRecordRepository deliveryRecordRepository;

    public Contract createContract(Contract contract) {
        if (contract.getBaseContractValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Base contract value must be positive");
        }
        return contractRepository.save(contract);
    }

    public Contract getContractById(Long id) {
        return contractRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Contract not found: " + id));
    }

    public Contract updateContract(Long id, Contract updated) {
        Contract existing = getContractById(id);
        existing.setTitle(updated.getTitle());
        existing.setCounterpartyName(updated.getCounterpartyName());
        existing.setAgreedDeliveryDate(updated.getAgreedDeliveryDate());
        existing.setBaseContractValue(updated.getBaseContractValue());
        return contractRepository.save(existing);
    }

    public void updateContractStatus(Long contractId) {
        Contract contract = getContractById(contractId);
        var latestDelivery = deliveryRecordRepository.findFirstByContractIdOrderByDeliveryDateDesc(contractId);
        if (latestDelivery.isEmpty()) {
            contract.setStatus(ContractStatus.ACTIVE);
        } else if (latestDelivery.get().getDeliveryDate().isAfter(contract.getAgreedDeliveryDate())) {
            contract.setStatus(ContractStatus.BREACHED);
        }
        contractRepository.save(contract);
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }
}
