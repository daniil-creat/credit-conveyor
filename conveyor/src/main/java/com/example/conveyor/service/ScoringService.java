package com.example.conveyor.service;

import com.example.conveyor.dto.EmploymentDTO;
import com.example.conveyor.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ScoringService {

    BigDecimal calculatingBaseRate(boolean isInsuranceEnabled, boolean isSalaryClient);

    BigDecimal calculatingCreditRate(boolean isInsuranceEnabled, boolean isSalaryClient, EmploymentDTO employment, MaritalStatus maritalStatus);

    boolean validationAge(LocalDate birthday);

    BigDecimal calculatingTotalAmount(BigDecimal rate, BigDecimal requestedAmount, Integer term);

    BigDecimal calculatingMonthlyPayment(BigDecimal rate, BigDecimal amount, Integer term);
}
