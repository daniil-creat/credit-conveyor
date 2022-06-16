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

    private final boolean[] isSalaryClient = {false, true, false, true};

    private final boolean[] isInsuranceEnabled = {false, false, true, true};

    @Override
    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        try {
            List<LoanOfferDTO> listLoanOfferDto = new ArrayList<>();
            BigDecimal requestedAmount = loanApplicationRequest.getAmount();
            if (validationAge(loanApplicationRequest.getBirthday())) {
                for (int i = 0; i < 4; i++) {
                    listLoanOfferDto.add(calculationLoanOfferDto(i, requestedAmount, loanApplicationRequest));
                }
            }
            return listLoanOfferDto;
        } catch (AgeException ex) {
            throw new AgeException("Error validation age");
        } catch (Exception ex) {
            throw new ScoringException("Error prescoring");
        }
    }

    private boolean validationAge(LocalDate birthday) throws AgeException {
        int years = Period.between(birthday, LocalDate.now()).getYears();
        if (years >= 18)
            return true;
        else
            throw new AgeException();
    }

    private LoanOfferDTO calculationLoanOfferDto(int i, BigDecimal requestedAmount, LoanApplicationRequestDTO loanApplicationRequest) {
        BigDecimal rate = baseRate;
        BigDecimal totalAmount = BigDecimal.valueOf(0);
        BigDecimal requestedAmountWithInsurance = requestedAmount.add(BigDecimal.valueOf(1000));
        if (!isInsuranceEnabled[i] && !isSalaryClient[i]) {
            totalAmount = calculatingTotalAmount(rate, requestedAmount, loanApplicationRequest.getTerm());

        } else if (!isInsuranceEnabled[i] && isSalaryClient[i]) {
            rate = rate.subtract(BigDecimal.valueOf(1));
            totalAmount = calculatingTotalAmount(rate, requestedAmount, loanApplicationRequest.getTerm());

        } else if (isInsuranceEnabled[i] && !isSalaryClient[i]) {
            rate = rate.subtract(BigDecimal.valueOf(3));

            totalAmount = calculatingTotalAmount(rate, requestedAmountWithInsurance, loanApplicationRequest.getTerm());

        } else if (isInsuranceEnabled[i] && isSalaryClient[i]) {
            rate = rate.subtract(BigDecimal.valueOf(4));
            totalAmount = calculatingTotalAmount(rate, requestedAmountWithInsurance, loanApplicationRequest.getTerm());
        }
        log.info("Get loan offers: total amount = {}, rate = {}", totalAmount, rate);
        BigDecimal monthlyPayment = calculatingMonthlyPayment(rate, requestedAmount, loanApplicationRequest.getTerm());

        return LoanOfferDTO.builder().applicationId((long) i + 1)
                .rate(rate)
                .requestedAmount(requestedAmount)
                .totalAmount(totalAmount)
                .term(loanApplicationRequest.getTerm())
                .isInsuranceEnabled(isInsuranceEnabled[i])
                .isSalaryClient(isSalaryClient[i])
                .monthlyPayment(monthlyPayment)
                .build();
    }

    private BigDecimal calculatingTotalAmount(BigDecimal rate, BigDecimal requestedAmount, Integer term) {
        BigDecimal termBigDecimal = BigDecimal.valueOf(term);
        return calculatingMonthlyPayment(rate, requestedAmount, term).multiply(termBigDecimal).setScale(2, RoundingMode.CEILING);
    }

    private BigDecimal calculatingMonthlyPayment(BigDecimal rate, BigDecimal amount, Integer term) {
        BigDecimal varP = rate.divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING).divide(BigDecimal.valueOf(12), 4, RoundingMode.CEILING);
        BigDecimal one = BigDecimal.valueOf(1);
        return amount.multiply(varP.add(varP.divide(one.add(varP).pow(term).subtract(one), 4, RoundingMode.CEILING)))
                .setScale(2, RoundingMode.CEILING);
    }
}
