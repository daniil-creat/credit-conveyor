package com.example.conveyor;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.EmploymentDTO;
import com.example.conveyor.dto.PaymentScheduleElement;
import com.example.conveyor.dto.ScoringDataDTO;
import com.example.conveyor.enums.EmploymentStatus;
import com.example.conveyor.enums.Gender;
import com.example.conveyor.enums.MaritalStatus;
import com.example.conveyor.enums.Position;
import com.example.conveyor.exceptions.AgeException;
import com.example.conveyor.exceptions.CreditException;
import com.example.conveyor.exceptions.ScoringException;
import com.example.conveyor.service.impl.CreditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CreditServiceTests {

    @Autowired
    private CreditServiceImpl creditService;

    ScoringDataDTO scoringData = new ScoringDataDTO();

    @BeforeEach
    public void setup() {
        EmploymentDTO employmentDTO = new EmploymentDTO();
        employmentDTO.setSalary(BigDecimal.valueOf(100000));
        employmentDTO.setPosition(Position.WORKER);
        employmentDTO.setEmploymentStatus(EmploymentStatus.WORKER);
        scoringData.setBirthdate(LocalDate.of(1996, 3, 8));
        scoringData.setAmount(BigDecimal.valueOf(200000));
        scoringData.setIsSalaryClient(false);
        scoringData.setIsInsuranceEnabled(false);
        scoringData.setTerm(6);
        scoringData.setGender(Gender.MALE);
        scoringData.setEmployment(employmentDTO);
        scoringData.setMaritalStatus(MaritalStatus.SINGLE);
    }

    @Test
    void getCreditTest() throws ScoringException, AgeException {
        CreditDTO creditDTO = creditService.getCredit(scoringData);
        List<PaymentScheduleElement> paymentScheduleElements = creditDTO.getPaymentSchedule();
        assertEquals(0, creditDTO.getAmount().compareTo(BigDecimal.valueOf(207120.00)));
        assertEquals(6, creditDTO.getTerm());
        assertEquals(0, creditDTO.getMonthlyPayment().compareTo(BigDecimal.valueOf(34520.00)));
        assertEquals(0, creditDTO.getRate().compareTo(BigDecimal.valueOf(12)));
        assertEquals(0, creditDTO.getPsk().compareTo(BigDecimal.valueOf(3.56)));
        assertEquals(false, creditDTO.getIsInsuranceEnabled());
        assertEquals(false, creditDTO.getIsSalaryClient());
        assertEquals(6, paymentScheduleElements.size());
    }

    @Test
    void getCreditAgeExceptionTest() {
        scoringData.setBirthdate(LocalDate.of(2010, 3, 8));
        assertThrows(AgeException.class, () -> {
            creditService.getCredit(scoringData);
        });
    }

    @Test
    void getCreditScorintDataNull() {
        ScoringDataDTO scoringDataDTO = null;
         assertThrows(RuntimeException.class, () -> {
            creditService.getCredit(scoringDataDTO);
        });
    }

    @Test
    void getCreditExceptionTest() {
        scoringData.setTerm(1000000000);
        scoringData.setAmount(BigDecimal.valueOf(1000000000));
         assertThrows(RuntimeException.class, () -> {
            creditService.getCredit(scoringData);
        });
    }

    @Test
    void getCreditIfSalaryClientTrue() throws ScoringException, AgeException {
        scoringData.setIsSalaryClient(true);
        CreditDTO creditDTO = creditService.getCredit(scoringData);
        List<PaymentScheduleElement> paymentScheduleElements = creditDTO.getPaymentSchedule();
        assertEquals(0, creditDTO.getAmount().compareTo(BigDecimal.valueOf(206520.00)));
        assertEquals(6, creditDTO.getTerm());
        assertEquals(0, creditDTO.getMonthlyPayment().compareTo(BigDecimal.valueOf(34420.00)));
        assertEquals(0, creditDTO.getRate().compareTo(BigDecimal.valueOf(11)));
        assertEquals(0, creditDTO.getPsk().compareTo(BigDecimal.valueOf(3.26)));
        assertEquals(false, creditDTO.getIsInsuranceEnabled());
        assertEquals(true, creditDTO.getIsSalaryClient());
        assertEquals(6, paymentScheduleElements.size());
    }

    @Test
    void getCreditIfSalaryClientTrueAndIsInsuranceEnabledTrue() throws ScoringException, AgeException {
        scoringData.setIsSalaryClient(true);
        scoringData.setIsInsuranceEnabled(true);
        CreditDTO creditDTO = creditService.getCredit(scoringData);
        List<PaymentScheduleElement> paymentScheduleElements = creditDTO.getPaymentSchedule();
        assertEquals(0, creditDTO.getAmount().compareTo(BigDecimal.valueOf(205743.60)));
        assertEquals(6, creditDTO.getTerm());
        assertEquals(0, creditDTO.getMonthlyPayment().compareTo(BigDecimal.valueOf(34290.60)));
        assertEquals(0, creditDTO.getRate().compareTo(BigDecimal.valueOf(8)));
        assertEquals(0, creditDTO.getPsk().compareTo(BigDecimal.valueOf(2.88)));
        assertEquals(true, creditDTO.getIsInsuranceEnabled());
        assertEquals(true, creditDTO.getIsSalaryClient());
        assertEquals(6, paymentScheduleElements.size());
    }

    @Test
    void getEmptyCredit() throws ScoringException, AgeException {
        scoringData.setIsSalaryClient(true);
        scoringData.setIsInsuranceEnabled(true);
        scoringData.setGender(Gender.FEMALE);
        scoringData.setMaritalStatus(MaritalStatus.SINGLE);
        assertThrows(CreditException.class, () -> {
            creditService.getCredit(scoringData);
        });
    }
}