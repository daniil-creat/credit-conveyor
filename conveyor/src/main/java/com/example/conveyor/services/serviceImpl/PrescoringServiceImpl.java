package com.example.conveyor.services.serviceImpl;

import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;
import com.example.conveyor.exceptions.AgeException;
import com.example.conveyor.exceptions.ScoringException;
import com.example.conveyor.services.PrescoringService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class PrescoringServiceImpl implements PrescoringService {

    @Value("${conveyor.base.rate}")
    private BigDecimal baseRate;

    @Override
    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        try {
            List<LoanOfferDTO> listLoanOfferDto = new ArrayList<>();
            boolean isSalaryClient = true;
            boolean isInsuranceEnabled = true;
            if (validationAge(loanApplicationRequest.getBirthday())) {
                listLoanOfferDto.add(calculatingLoanOffer(!isInsuranceEnabled, !isSalaryClient, loanApplicationRequest, 1L));
                listLoanOfferDto.add(calculatingLoanOffer(!isInsuranceEnabled, isSalaryClient, loanApplicationRequest, 2L));
                listLoanOfferDto.add(calculatingLoanOffer(isInsuranceEnabled, !isSalaryClient, loanApplicationRequest, 3L));
                listLoanOfferDto.add(calculatingLoanOffer(isInsuranceEnabled, isSalaryClient, loanApplicationRequest, 4L));
            }
            return listLoanOfferDto;
        } catch (AgeException ex) {
            throw new AgeException("Error validation age");
        } catch (Exception ex) {
            throw new ScoringException("Error prescoring");
        }
    }

    @Override
    public boolean validationAge(LocalDate birthday) throws AgeException {
        int years = Period.between(birthday, LocalDate.now()).getYears();
        if (years >= 18)
            return true;
        else
            throw new AgeException();
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

    @Override
    public BigDecimal calculatingRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal rate = baseRate;
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(1));
        }
        if (isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        }
        return rate;
    }

    private LoanOfferDTO calculatingLoanOffer(boolean isInsuranceEnabled, boolean isSalaryClient, LoanApplicationRequestDTO loanApplicationRequest, Long id) {
        BigDecimal rate = calculatingRate(isInsuranceEnabled, isSalaryClient);
        BigDecimal requestedAmount = loanApplicationRequest.getAmount();
        BigDecimal requestedAmountWithInsurance = requestedAmount.add(BigDecimal.valueOf(1000));
        requestedAmount = isInsuranceEnabled ? requestedAmountWithInsurance : requestedAmount;
        BigDecimal totalAmount = calculatingTotalAmount(rate, requestedAmount, loanApplicationRequest.getTerm());
        log.info("Get loan offers: total amount = {}, rate = {}", totalAmount, rate);
        BigDecimal monthlyPayment = calculatingMonthlyPayment(rate, requestedAmount, loanApplicationRequest.getTerm());
        return LoanOfferDTO.builder().applicationId(id)
                .rate(rate)
                .requestedAmount(loanApplicationRequest.getAmount())
                .totalAmount(totalAmount)
                .term(loanApplicationRequest.getTerm())
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .monthlyPayment(monthlyPayment)
                .build();
    }
}
