package com.example.conveyor.service.impl;

import com.example.conveyor.dto.EmploymentDTO;
import com.example.conveyor.enums.MaritalStatus;
import com.example.conveyor.exceptions.AgeException;
import com.example.conveyor.service.ScoringService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

import static com.example.conveyor.enums.MaritalStatus.MARRIED;
import static com.example.conveyor.enums.Position.*;

@Service
public class ScoringServiceImpl  implements ScoringService {

    @Value("${conveyor.base.rate}")
    private BigDecimal baseRate;

    @Override
    public BigDecimal calculatingBaseRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal rate = baseRate;
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.ONE);
        }
        if (isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        }
        return rate;
    }

    @Override
    public BigDecimal calculatingCreditRate(boolean isInsuranceEnabled, boolean isSalaryClient, EmploymentDTO employment, MaritalStatus maritalStatus) {
       BigDecimal rate = baseRate;
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.ONE);
        }
        if (isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        }
        if (employment.getPosition().equals(JUDGE) || employment.getPosition().equals(SOLDIER)) {
            rate = rate.subtract(BigDecimal.ONE);
        }
        if (employment.getPosition().equals(SPECIALIST)) {
            rate = rate.subtract(BigDecimal.valueOf(0.5));
        }
        if (maritalStatus.equals(MARRIED)) {
            rate = rate.subtract(BigDecimal.ONE);
        }
        return rate;
    }

    @Override
    public boolean validationAge(LocalDate birthday) throws AgeException {
        int years = Period.between(birthday, LocalDate.now()).getYears();
        if (years >= 18)
            return true;
        else
            throw new AgeException("Age less than eighteen");
    }

    @Override
    public BigDecimal calculatingTotalAmount(BigDecimal rate, BigDecimal requestedAmount, Integer term) {
        return calculatingMonthlyPayment(rate, requestedAmount, term).multiply(BigDecimal.valueOf(term)).setScale(2, RoundingMode.CEILING);
    }

    @Override
    public BigDecimal calculatingMonthlyPayment(BigDecimal rate, BigDecimal amount, Integer term) {
        BigDecimal intermediateCoefficient = rate.divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING).divide(BigDecimal.valueOf(12), 4, RoundingMode.CEILING);
        BigDecimal one = BigDecimal.valueOf(1);
        return amount.multiply(intermediateCoefficient.add(intermediateCoefficient.divide(one.add(intermediateCoefficient).pow(term).subtract(one), 4, RoundingMode.CEILING)))
                .setScale(2, RoundingMode.CEILING);
    }
}
