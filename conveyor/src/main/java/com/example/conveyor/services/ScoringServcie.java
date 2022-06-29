package com.example.conveyor.services;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.EmploymentDTO;
import com.example.conveyor.dto.ScoringDataDTO;
import com.example.conveyor.enums.Gender;
import com.example.conveyor.enums.MaritalStatus;

import java.math.BigDecimal;

public interface ScoringServcie {

    CreditDTO getCredit(ScoringDataDTO scoringData);

    BigDecimal calculatingRate(boolean isInsuranceEnabled, boolean isSalaryClient, EmploymentDTO employment, MaritalStatus maritalStatus);

    BigDecimal calculatingTotalAmount(BigDecimal rate, BigDecimal requestedAmount, Integer term, EmploymentDTO employment, Gender gender, MaritalStatus maritalStatus);
}
