package com.example.demo.repository;

import com.example.demo.entity.BreachReport;
import com.example.demo.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BreachReportRepository extends JpaRepository<BreachReport, Long> {
    List<BreachReport> findByContractIdOrderByReportedAtDesc(Long contractId);
}
