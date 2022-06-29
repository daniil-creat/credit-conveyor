package com.example.conveyor.services.serviceImpl;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.EmploymentDTO;
import com.example.conveyor.dto.PaymentScheduleElement;
import com.example.conveyor.dto.ScoringDataDTO;
import com.example.conveyor.enums.Gender;
import com.example.conveyor.enums.MaritalStatus;
import com.example.conveyor.exceptions.AgeException;
import com.example.conveyor.exceptions.ScoringException;
import com.example.conveyor.services.PrescoringService;
import com.example.conveyor.services.ScoringServcie;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static com.example.conveyor.enums.EmploymentStatus.UNEMPLOYED;
import static com.example.conveyor.enums.Gender.FEMALE;
import static com.example.conveyor.enums.MaritalStatus.MARRIED;
import static com.example.conveyor.enums.MaritalStatus.SINGLE;
import static com.example.conveyor.enums.Position.JUDGE;
import static com.example.conveyor.enums.Position.SOLDIER;
import static java.util.Collections.emptyList;

@Log4j2
@Service
public class ScoringServiceImpl implements ScoringServcie {

    @Value("${conveyor.base.rate}")
    private BigDecimal baseRate;

    @Autowired
    private PrescoringService prescoringService;

    @Override
    public CreditDTO getCredit(ScoringDataDTO scoringData) {
        try {
            BigDecimal amountWithInsurance = scoringData.getAmount().add(BigDecimal.valueOf(1000));
            BigDecimal amount = scoringData.getIsInsuranceEnabled() ? amountWithInsurance : scoringData.getAmount();
            EmploymentDTO employment = scoringData.getEmployment();
            CreditDTO creditDTO = null;
            if (prescoringService.validationAge(scoringData.getBirthdate())) {
                if (isNotPayingClient(scoringData.getAmount(), scoringData.getTerm(), scoringData.getEmployment(), scoringData.getGender(), scoringData.getMaritalStatus())) {
                    return CreditDTO.builder()
                            .amount(BigDecimal.ZERO)
                            .rate(BigDecimal.ZERO)
                            .term(0)
                            .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                            .isSalaryClient(scoringData.getIsSalaryClient())
                            .monthlyPayment(BigDecimal.ZERO)
                            .psk(BigDecimal.ZERO)
                            .paymentSchedule(emptyList())
                            .build();
                }
                BigDecimal rate = calculatingRate(scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient(), scoringData.getEmployment(), scoringData.getMaritalStatus());
                BigDecimal totalAmount = calculatingTotalAmount(rate, amount, scoringData.getTerm(), scoringData.getEmployment(), scoringData.getGender(), scoringData.getMaritalStatus());
                BigDecimal payment = prescoringService.calculatingMonthlyPayment(rate, amount, scoringData.getTerm());
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

    @Override
    public BigDecimal calculatingRate(boolean isInsuranceEnabled, boolean isSalaryClient, EmploymentDTO employment, MaritalStatus maritalStatus) {
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
        if (maritalStatus.equals(MARRIED)) {
            rate = rate.subtract(BigDecimal.ONE);
        }
        return rate;
    }

    @Override
    public BigDecimal calculatingTotalAmount(BigDecimal rate, BigDecimal requestedAmount, Integer term, EmploymentDTO employment, Gender gender, MaritalStatus maritalStatus) {
        return prescoringService.calculatingMonthlyPayment(rate, requestedAmount, term).multiply(BigDecimal.valueOf(term)).setScale(2, RoundingMode.CEILING);
    }

    private boolean isNotPayingClient(BigDecimal amount, Integer term, EmploymentDTO employment, Gender gender, MaritalStatus maritalStatus) {
        return amount.divide(BigDecimal.valueOf(term), RoundingMode.CEILING)
                .compareTo(employment.getSalary()) > 0
                || employment.getEmploymentStatus().equals(UNEMPLOYED)
                || (gender.equals(FEMALE) && maritalStatus.equals(SINGLE));
    }

    private BigDecimal calculatingPsk(BigDecimal newAmount, BigDecimal amount, Integer term) {
        BigDecimal one = BigDecimal.valueOf(1);
        return (newAmount.divide(amount, 4, RoundingMode.CEILING).subtract(one)).divide(BigDecimal.valueOf(term)
                        .divide(BigDecimal.valueOf(12), RoundingMode.CEILING), 4, RoundingMode.CEILING)
                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.CEILING);
    }

    private List<PaymentScheduleElement> calculatingPaymentScheduleElements(BigDecimal remainingDebt, Integer term, BigDecimal rate, BigDecimal payment) {
        List<PaymentScheduleElement> paymentScheduleElements = new LinkedList<>();
        BigDecimal intermediateCoefficient = rate.divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING).divide(BigDecimal.valueOf(12), 4, RoundingMode.CEILING);
        LocalDate date = LocalDate.now();
        for (int i = 1; i <= term; i++) {
            BigDecimal newAmount = remainingDebt;
            remainingDebt = remainingDebt.subtract(payment);
            date = date.plusMonths(1);
            BigDecimal procents = newAmount.multiply(intermediateCoefficient).setScale(2, RoundingMode.CEILING);
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
