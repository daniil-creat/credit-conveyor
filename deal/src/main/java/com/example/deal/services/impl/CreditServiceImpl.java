package com.example.deal.services.impl;

import com.example.deal.dto.CreditDTO;
import com.example.deal.entity.Credit;
import com.example.deal.repository.CreditRepository;
import com.example.deal.services.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    @Override
    public Credit saveCredit(CreditDTO credit) {
        Credit creditNew = Credit.builder()
                .amount(credit.getAmount())
                .isInsuranceEnabled(credit.getIsInsuranceEnabled())
                .isSalaryClient(credit.getIsSalaryClient())
                .monthlyPayment(credit.getMonthlyPayment())
                .term(credit.getTerm())
                .paymentSchedule(credit.getPaymentSchedule())
                .build();
        return creditRepository.save(creditNew);
    }
}
