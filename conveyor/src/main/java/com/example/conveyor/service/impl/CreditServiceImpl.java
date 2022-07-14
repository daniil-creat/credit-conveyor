package com.example.conveyor.service.impl;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.EmploymentDTO;
import com.example.conveyor.dto.PaymentScheduleElement;
import com.example.conveyor.dto.ScoringDataDTO;
import com.example.conveyor.exceptions.CreditException;
import com.example.conveyor.service.CreditServcie;
import com.example.conveyor.service.ScoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static com.example.conveyor.enums.EmploymentStatus.UNEMPLOYED;
import static com.example.conveyor.enums.Gender.FEMALE;
import static com.example.conveyor.enums.MaritalStatus.SINGLE;
import static com.example.conveyor.enums.Position.INTERMEDIATE_MANAGEMENT;
import static com.example.conveyor.enums.Position.TOP_MANAGEMENT;

@Log4j2
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditServcie {

    private final ScoringService scoringService;

    @Override
    public CreditDTO getCredit(ScoringDataDTO scoringData) {
        EmploymentDTO employment = scoringData.getEmployment();
        BigDecimal amount = scoringData.getIsInsuranceEnabled() ? calculatingAmountWithInsurance(scoringData.getAmount(), employment) : scoringData.getAmount();
        if (isPayingClient(scoringData)) {
            BigDecimal rate = scoringService.calculatingCreditRate(scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient(), scoringData.getEmployment(), scoringData.getMaritalStatus());
            BigDecimal totalAmount = scoringService.calculatingTotalAmount(rate, amount, scoringData.getTerm());
            BigDecimal payment = scoringService.calculatingMonthlyPayment(rate, amount, scoringData.getTerm());
            BigDecimal psk = calculatingPsk(totalAmount, scoringData.getAmount(), scoringData.getTerm());
            log.info("Get credits: amount = {}, rate = {}, newTotalAmount = {}, payment = {}, psk = {}", amount, rate, totalAmount, payment, psk);
            List<PaymentScheduleElement> paymentScheduleElements = calculatingPaymentScheduleElements(totalAmount, scoringData.getTerm(), rate, payment);
            return CreditDTO.builder()
                    .amount(totalAmount)
                    .rate(rate)
                    .term(scoringData.getTerm())
                    .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                    .isSalaryClient(scoringData.getIsSalaryClient())
                    .monthlyPayment(payment)
                    .psk(psk)
                    .paymentSchedule(paymentScheduleElements)
                    .build();
        } else {
            throw new CreditException("credit denied");
        }
    }

    private BigDecimal calculatingAmountWithInsurance(BigDecimal amount, EmploymentDTO employment) {
        boolean isNotManager = !employment.getPosition().equals(INTERMEDIATE_MANAGEMENT) && !employment.getPosition().equals(TOP_MANAGEMENT);
        return isNotManager ? amount.add(BigDecimal.valueOf(1000)) : amount;
    }

    private boolean isPayingClient(ScoringDataDTO scoringData) {
        EmploymentDTO employment = scoringData.getEmployment();
        if (!scoringService.validationAge(scoringData.getBirthdate())) {
            return false;
        }
        if (!isSalaryGreaterThanMonthlyPayment(scoringData, employment)) {
            return false;
        }
        if (employment.getEmploymentStatus().equals(UNEMPLOYED)) {
            return false;
        }
        if (scoringData.getGender().equals(FEMALE) && scoringData.getMaritalStatus().equals(SINGLE)){
            return false;
        }
            return true;
    }

    private boolean isSalaryGreaterThanMonthlyPayment(ScoringDataDTO scoringData, EmploymentDTO employment) {
        return scoringData.getAmount().divide(BigDecimal.valueOf(scoringData.getTerm()), RoundingMode.CEILING).compareTo(employment.getSalary()) < 0;
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
