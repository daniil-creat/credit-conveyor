package com.example.conveyor.services;

import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PrescoringService {

    List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequest);

    boolean validationAge(LocalDate birthday);

    BigDecimal calculatingTotalAmount(BigDecimal rate, BigDecimal requestedAmount, Integer term);

    BigDecimal calculatingMonthlyPayment(BigDecimal rate, BigDecimal amount, Integer term);

    BigDecimal calculatingRate(boolean isInsuranceEnabled, boolean isSalaryClient);

}
