package com.example.deal.services.impl;

import com.example.deal.dto.CreditDTO;
import com.example.deal.entity.Credit;
import com.example.deal.repository.CreditRepository;
import com.example.deal.services.CreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    @Override
    public Credit saveCredit(CreditDTO credit) {
        log.info("Service: Credit, save method, parameters:{}", credit);
        Credit creditNew = Credit.builder()
                .amount(credit.getAmount())
                .isInsuranceEnabled(credit.getIsInsuranceEnabled())
                .isSalaryClient(credit.getIsSalaryClient())
                .monthlyPayment(credit.getMonthlyPayment())
                .term(credit.getTerm())
                .paymentSchedule(credit.getPaymentSchedule())
                .build();
        Credit savedCredit = creditRepository.save(creditNew);
        log.info("Service: Credit, save method, return:{}", savedCredit);
        return savedCredit;
    }
}
