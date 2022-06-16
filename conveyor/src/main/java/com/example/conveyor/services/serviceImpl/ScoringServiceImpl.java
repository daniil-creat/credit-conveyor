package com.example.conveyor.services.serviceImpl;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.PaymentScheduleElement;
import com.example.conveyor.dto.ScoringDataDTO;
import com.example.conveyor.exceptions.AgeException;
import com.example.conveyor.exceptions.ScoringException;
import com.example.conveyor.services.ScoringServcie;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;
import java.util.List;

@Log4j2
@Service
public class ScoringServiceImpl implements ScoringServcie {

    @Value("${conveyor.base.rate}")
    private BigDecimal baseRate;

    @Override
    public CreditDTO getCredit(ScoringDataDTO scoringData) {
        try {
            BigDecimal amount = scoringData.getAmount();
            BigDecimal rate = baseRate;
            CreditDTO creditDTO = null;
            if (validationAge(scoringData.getBirthdate())) {
                if (scoringData.getIsSalaryClient()) {
                    rate = rate.subtract(BigDecimal.valueOf(1));
                }
                if (scoringData.getIsInsuranceEnabled()) {
                    rate = rate.subtract(BigDecimal.valueOf(3));
                    amount = amount.add(BigDecimal.valueOf(1000));
                }
                BigDecimal totalAmount = calculatingTotalAmount(rate, amount, scoringData.getTerm());
                BigDecimal payment = calculatingMonthlyPayment(rate, amount, scoringData.getTerm());
                BigDecimal psk = calculatingPsk(totalAmount, scoringData.getAmount(), scoringData.getTerm());
                log.info("Get credits: amount = {}, rate = {}, newTotalAmount = {}, payment = {}, psk = {}", amount, rate, totalAmount, payment, psk);
                List<PaymentScheduleElement> paymentScheduleElements = calculatingPaymentScheduleElements(totalAmount, scoringData.getTerm(), rate, payment);
                creditDTO = CreditDTO.builder()
                        .amount(totalAmount)
                        .rate(rate)
                        .term(scoringData.getTerm())
                        .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                        .isSalaryClient(scoringData.getIsSalaryClient())
                        .monthlyPayment(payment)
                        .psk(psk)
                        .paymentSchedule(paymentScheduleElements)
                        .build();
            }
            return creditDTO;
        } catch (AgeException e) {
            throw new AgeException("Error validate age");
        } catch (Exception e) {
            throw new ScoringException("Error scoring");
        }
    }

    private boolean validationAge(LocalDate birthday) throws AgeException {
        int years = Period.between(birthday, LocalDate.now()).getYears();
        if (years >= 18)
            return true;
        else
            throw new AgeException();
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

    private BigDecimal calculatingPsk(BigDecimal newAmount, BigDecimal amount, Integer term) {
        BigDecimal one = BigDecimal.valueOf(1);
        return (newAmount.divide(amount, 4, RoundingMode.CEILING).subtract(one)).divide(BigDecimal.valueOf(term)
                        .divide(BigDecimal.valueOf(12), RoundingMode.CEILING), 4, RoundingMode.CEILING)
                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.CEILING);
    }

    private List<PaymentScheduleElement> calculatingPaymentScheduleElements(BigDecimal remainingDebt, Integer term, BigDecimal rate, BigDecimal payment) {
        List<PaymentScheduleElement> paymentScheduleElements = new LinkedList<>();
        BigDecimal varP = rate.divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING).divide(BigDecimal.valueOf(12), 4, RoundingMode.CEILING);
        LocalDate date = LocalDate.now();
        for (int i = 1; i <= term; i++) {
            BigDecimal newAmount = remainingDebt;
            remainingDebt = remainingDebt.subtract(payment);
            date = date.plusMonths(1);
            BigDecimal procents = newAmount.multiply(varP).setScale(2, RoundingMode.CEILING);
            paymentScheduleElements.add(PaymentScheduleElement
                    .builder()
                    .number(i)
                    .date(date)
                    .totalPayment(payment)
                    .remainingDebt(remainingDebt)
                    .debtPayment(payment.subtract(procents))
                    .interestPayment(procents)
                    .build());
        }
        return paymentScheduleElements;
    }
}
