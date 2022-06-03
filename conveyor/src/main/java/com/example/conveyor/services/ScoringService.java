package com.example.conveyor.services;

import com.example.conveyor.exceptions.AgeException;
import com.example.conveyor.exceptions.ScoringException;
import com.example.conveyor.dto.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Log4j2
@Service
public class ScoringService {

    @Value("${conveyor.base.rate}")
    private BigDecimal baseRate;

    private final boolean[] isSalaryClient = {false, true, false, true};

    private final boolean[] isInsuranceEnabled = {false, false, true, true};

    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) throws ScoringException, AgeException {
        try {
            BigDecimal totalAmount = BigDecimal.valueOf(0);
            List<LoanOfferDTO> listLoanOfferDto = new ArrayList<>();
            if (validationAge(loanApplicationRequest.getBirthday())) {
                for (int i = 0; i < 4; i++) {
                    BigDecimal rate = baseRate;
                    BigDecimal newAmount = BigDecimal.valueOf(1000);
                    if (!isInsuranceEnabled[i] && !isSalaryClient[i]) {
                        newAmount = loanApplicationRequest.getAmount();
                        totalAmount = getNewTotalAmount(rate, newAmount, loanApplicationRequest.getTerm());

                    } else if (!isInsuranceEnabled[i] && isSalaryClient[i]) {
                        rate = rate.subtract(BigDecimal.valueOf(1));
                        newAmount = loanApplicationRequest.getAmount();
                        totalAmount = getNewTotalAmount(rate, newAmount, loanApplicationRequest.getTerm());

                    } else if (isInsuranceEnabled[i] && !isSalaryClient[i]) {
                        rate = rate.subtract(BigDecimal.valueOf(3));
                        newAmount = newAmount.add(loanApplicationRequest.getAmount());
                        totalAmount = getNewTotalAmount(rate, newAmount, loanApplicationRequest.getTerm());

                    } else if (isInsuranceEnabled[i] && isSalaryClient[i]) {
                        rate = rate.subtract(BigDecimal.valueOf(4));
                        newAmount = newAmount.add(loanApplicationRequest.getAmount());
                        totalAmount = getNewTotalAmount(rate, newAmount, loanApplicationRequest.getTerm());
                    }
                    log.info("Get loan offers: total amount = {}, rate = {}", totalAmount, rate);
                    BigDecimal monthlyPayment = getPayment(rate, newAmount, loanApplicationRequest.getTerm());
                    listLoanOfferDto.add(
                            new LoanOfferDTO.LoanOfferDtoBuilder().setApplicationId((long) i + 1)
                                    .setRate(rate)
                                    .setRequestedAmount(loanApplicationRequest.getAmount())
                                    .setTotalAmount(totalAmount)
                                    .setTerm(loanApplicationRequest.getTerm())
                                    .setIsInsuranceEnabled(isInsuranceEnabled[i])
                                    .setIsSalaryClient(isSalaryClient[i])
                                    .setMonthlyPayment(monthlyPayment)
                                    .build()
                    );
                }
            }
            return listLoanOfferDto;
        } catch (AgeException ex) {
            throw new AgeException("Error validation age");
        } catch (Exception ex) {
            throw new ScoringException("Error prescoring");
        }
    }

    public CreditDTO getCredit(ScoringDataDTO scoringData) throws ScoringException, AgeException {
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
                BigDecimal newTotalAmount = getNewTotalAmount(rate, amount, scoringData.getTerm());
                BigDecimal payment = getPayment(rate, amount, scoringData.getTerm());
                BigDecimal psk = getPsk(newTotalAmount, scoringData.getAmount(), scoringData.getTerm());
                log.info("Get credits: amount = {}, rate = {}, newTotalAmount = {}, payment = {}, psk = {}",amount, rate, newTotalAmount,payment, psk);
                List<PaymentScheduleElement> paymentScheduleElements = getPaymentScheduleElements(newTotalAmount, scoringData.getTerm(), rate, payment);
                creditDTO = new CreditDTO.CreditDtoBuilder()
                        .setAmount(newTotalAmount)
                        .setRate(rate)
                        .setTerm(scoringData.getTerm())
                        .setIsInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                        .setIsSalaryClient(scoringData.getIsSalaryClient())
                        .setMonthlyPayment(payment)
                        .setPsk(psk)
                        .setPaymentSchedule(paymentScheduleElements)
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

    private BigDecimal getNewTotalAmount(BigDecimal rate, BigDecimal amount, Integer term) {
        BigDecimal termBigDecimal = BigDecimal.valueOf(term);
        return getPayment(rate, amount, term).multiply(termBigDecimal).setScale(2, RoundingMode.CEILING);
    }

    private BigDecimal getPayment(BigDecimal rate, BigDecimal amount, Integer term) {
        BigDecimal varP = getVarP(rate);
        BigDecimal one = BigDecimal.valueOf(1);
        return amount.multiply(varP.add(varP.divide(one.add(varP).pow(term).subtract(one), 4, RoundingMode.CEILING)))
                .setScale(2, RoundingMode.CEILING);
    }

    private BigDecimal getPsk(BigDecimal newAmount, BigDecimal amount, Integer term) {
        BigDecimal one = BigDecimal.valueOf(1);
        return (newAmount.divide(amount, 4, RoundingMode.CEILING).subtract(one)).divide(BigDecimal.valueOf(term)
                        .divide(BigDecimal.valueOf(12), RoundingMode.CEILING), 4, RoundingMode.CEILING)
                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.CEILING);
    }

    private List<PaymentScheduleElement> getPaymentScheduleElements(BigDecimal remainingDebt, Integer term, BigDecimal rate, BigDecimal payment) {
        List<PaymentScheduleElement> paymentScheduleElements = new LinkedList<>();
        LocalDate date = LocalDate.now();
        for (int i = 1; i <= term; i++) {
            BigDecimal newAmount = remainingDebt;
            remainingDebt = remainingDebt.subtract(payment);
            date = date.plusMonths(1);
            BigDecimal procents = newAmount.multiply(getVarP(rate)).setScale(2, RoundingMode.CEILING);
            log.info("Block paymentScheduleElements: remainingDebt = {}, date = {}, procents = {}", remainingDebt, date, procents);
            paymentScheduleElements.add(new PaymentScheduleElement
                    .PaymentScheduleElementBuilder()
                    .setNumber(i)
                    .setDate(date)
                    .setTotalPayment(payment)
                    .setRemainingDebt(remainingDebt)
                    .setDebtPayment(payment.subtract(procents))
                    .setInterestPayment(procents)
                    .build());
        }
        return paymentScheduleElements;
    }

    private BigDecimal getVarP(BigDecimal rate) {
        return rate.divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING).divide(BigDecimal.valueOf(12), 4, RoundingMode.CEILING);
    }
}
