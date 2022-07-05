package com.example.conveyor;

import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;
import com.example.conveyor.exceptions.AgeException;
import com.example.conveyor.exceptions.ScoringException;
import com.example.conveyor.service.impl.OfferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.util.Assert.isTrue;

@SpringBootTest
public class OfferServiceTests {

    @Autowired
    private OfferServiceImpl offerService;

    LoanApplicationRequestDTO loanApplicationRequest = new LoanApplicationRequestDTO();

    @BeforeEach
    public void setup() {
        List<LoanOfferDTO> loanOfferDTOS = new ArrayList<>();
        LoanOfferDTO loanOfferDto1;
        LoanOfferDTO loanOfferDto2;
        LoanOfferDTO loanOfferDto3;
        LoanOfferDTO loanOfferDto4;
        loanApplicationRequest.setAmount(BigDecimal.valueOf(200000));
        loanApplicationRequest.setTerm(6);
        loanApplicationRequest.setBirthday(LocalDate.of(1996, 3, 8));
    }

    @Test()
    public void getLoanOffersTest() throws ScoringException, AgeException {
        List<LoanOfferDTO> loanOfferDTOs = offerService.getLoanOffers(loanApplicationRequest);
        LoanOfferDTO loanOfferDTO1 = loanOfferDTOs.get(0);
        LoanOfferDTO loanOfferDTO2 = loanOfferDTOs.get(1);
        LoanOfferDTO loanOfferDTO3 = loanOfferDTOs.get(2);
        LoanOfferDTO loanOfferDTO4 = loanOfferDTOs.get(3);

        isTrue(loanOfferDTOs.size() == 4, "Error size collection");
        isTrue(loanOfferDTO1.getApplicationId() == 1L, "No equals applicationId");
        isTrue(loanOfferDTO2.getApplicationId() == 2L, "No equals applicationId");
        isTrue(loanOfferDTO3.getApplicationId() == 3L, "No equals applicationId");
        isTrue(loanOfferDTO4.getApplicationId() == 4L, "No equals applicationId");
        isTrue(loanOfferDTO1.getTerm() == 6, "No equals Term");
        isTrue(loanOfferDTO2.getTerm() == 6, "No equals Term");
        isTrue(loanOfferDTO3.getTerm() == 6, "No equals Term");
        isTrue(loanOfferDTO4.getTerm() == 6, "No equals Term");
        isTrue(loanOfferDTO1.getRequestedAmount().equals(BigDecimal.valueOf(200000)), "No equals amount");
        isTrue(loanOfferDTO2.getRequestedAmount().equals(BigDecimal.valueOf(200000)), "No equals amount");
        isTrue(loanOfferDTO3.getRequestedAmount().equals(BigDecimal.valueOf(200000)), "No equals amount");
        isTrue(loanOfferDTO4.getRequestedAmount().equals(BigDecimal.valueOf(200000)), "No equals amount");
        isTrue(loanOfferDTO1.getTotalAmount().compareTo(BigDecimal.valueOf(207120.00)) == 0, "No equals totalAmount");
        isTrue(loanOfferDTO2.getTotalAmount().compareTo(BigDecimal.valueOf(206520.00)) == 0, "No equals totalAmount");
        isTrue(loanOfferDTO3.getTotalAmount().compareTo(BigDecimal.valueOf(206346.60)) == 0, "No equals totalAmount");
        isTrue(loanOfferDTO4.getTotalAmount().compareTo(BigDecimal.valueOf(205743.60)) == 0, "No equals totalAmount");
        isTrue(loanOfferDTO1.getRate().compareTo(BigDecimal.valueOf(12)) == 0, "No equals rate");
        isTrue(loanOfferDTO2.getRate().compareTo(BigDecimal.valueOf(11)) == 0, "No equals rate");
        isTrue(loanOfferDTO3.getRate().compareTo(BigDecimal.valueOf(9)) == 0, "No equals rate");
        isTrue(loanOfferDTO4.getRate().compareTo(BigDecimal.valueOf(8)) == 0, "No equals rate");
        isTrue(!loanOfferDTO1.getIsSalaryClient(), "No equals IsSalaryClient");
        isTrue(loanOfferDTO2.getIsSalaryClient(), "No equals IsSalaryClient");
        isTrue(!loanOfferDTO3.getIsSalaryClient(), "No equals IsSalaryClient");
        isTrue(loanOfferDTO4.getIsSalaryClient(), "No equals IsSalaryClient");
        isTrue(!loanOfferDTO1.getIsInsuranceEnabled(), "No equals IsInsuranceEnabled");
        isTrue(!loanOfferDTO2.getIsInsuranceEnabled(), "No equals IsInsuranceEnabled");
        isTrue(loanOfferDTO3.getIsInsuranceEnabled(), "No equals IsInsuranceEnabled");
        isTrue(loanOfferDTO4.getIsInsuranceEnabled(), "No equals IsInsuranceEnabled");
    }

    @Test
    void getLoanOffersErrorAgeTest() {
        loanApplicationRequest.setBirthday(LocalDate.of(2010, 3, 8));
        AgeException exception = assertThrows(AgeException.class, () -> {
           offerService.getLoanOffers(loanApplicationRequest);
        });
    }

    @Test
    void getLoanOffersLowByLoanApplicationRequestNullTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = null;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            offerService.getLoanOffers(loanApplicationRequestDTO);
        });
    }
}
