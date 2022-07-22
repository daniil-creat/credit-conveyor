package com.example.deal.repository;

import com.example.deal.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface CreditRepository extends JpaRepository<Credit, Long> {
}
