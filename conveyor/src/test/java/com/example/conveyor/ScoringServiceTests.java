package com.example.conveyor;

import com.example.conveyor.dto.EmploymentDTO;
import com.example.conveyor.enums.MaritalStatus;
import com.example.conveyor.enums.Position;
import com.example.conveyor.service.ScoringService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
public class ScoringServiceTests {

    @Autowired
    ScoringService scoringService;

    @Test
    public void calculatingBaseRateTrueTrueTest() {
        BigDecimal rate = BigDecimal.valueOf(12);
        rate = scoringService.calculatingBaseRate(true, true);
        Assert.isTrue(rate.compareTo(BigDecimal.valueOf(8)) == 0, "error");

    }

    @Test
    public void calculatingBaseRateTrueFalseTest() {
        BigDecimal rate = BigDecimal.valueOf(12);
        rate = scoringService.calculatingBaseRate(true, false);
        Assert.isTrue(rate.compareTo(BigDecimal.valueOf(9)) == 0, "error");

    }

    @Test
    public void calculatingBaseRateFalseTrueTest() {
        BigDecimal rate = BigDecimal.valueOf(12);
        rate = scoringService.calculatingBaseRate(false, true);
        Assert.isTrue(rate.compareTo(BigDecimal.valueOf(11)) == 0, "error");

    }

    @Test
    public void calculatingBaseRateFalseFalseTest() {
        BigDecimal rate = BigDecimal.valueOf(12);
        rate = scoringService.calculatingBaseRate(false, false);
        Assert.isTrue(rate.compareTo(BigDecimal.valueOf(12)) == 0, "error");
    }

    @Test
    public void calculatingCreditTest() {
        BigDecimal rate = BigDecimal.valueOf(12);
        EmploymentDTO employment = new EmploymentDTO();
        employment.setPosition(Position.SOLDIER);
        rate = scoringService.calculatingCreditRate(false, false, employment, MaritalStatus.MARRIED);
        Assert.isTrue(rate.compareTo(BigDecimal.valueOf(10)) == 0, "error");
    }

    @Test
    public void validationAgeTest() {
         scoringService.validationAge(LocalDate.of(1996,4,1));
        Assert.isTrue(true, "error");
    }

    @Test
    void calculatingMonthlyPaymentTest(){
        BigDecimal monthlyPayment = scoringService.calculatingMonthlyPayment(BigDecimal.valueOf(12),BigDecimal.valueOf(1000),6);
        Assert.isTrue(monthlyPayment.compareTo(BigDecimal.valueOf(172.6)) == 0, "error");
    }
}
