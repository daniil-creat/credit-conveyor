package com.example.application;

import com.example.application.dto.LoanApplicationRequestDTO;
import com.example.application.dto.LoanOfferDTO;
import com.example.application.proxy.ServiceProxy;
import com.example.application.services.ApplicationService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class ApplicationServiceTests {

    @Autowired
    private ApplicationService applicationService;

    @MockBean
    private ServiceProxy serviceProxy;

    @Test
    void getLoanOfferTest() throws IOException {

        LoanApplicationRequestDTO loanApplicationRequest = LoanApplicationRequestDTO.builder()
                .birthday(LocalDate.of(1996, 8, 3))
                .term(6)
                .passportNumber("524201")
                .lastName("Kalimov")
                .firstName("Daniil")
                .amount(BigDecimal.valueOf(200000))
                .email("dkal@mail.com")
                .middleName("Valeryevich")
                .passportSeries("4010")
                .build();

        List<LoanOfferDTO> loanOffers = List.of(new LoanOfferDTO(), new LoanOfferDTO(), new LoanOfferDTO(), new LoanOfferDTO());

        when(serviceProxy.createApplicationAndGetLoanOffers(any())).thenReturn(loanOffers);

        applicationService.getLoanOffers(loanApplicationRequest);

        verify(serviceProxy).createApplicationAndGetLoanOffers(loanApplicationRequest);
    }

    @Test
    void sendLoanOfferToDealTest() throws IOException {
        File file = ResourceUtils.getFile("classpath:loanOffer.json");
        String loanOfferStr = new String(Files.readAllBytes(file.toPath()));

        Gson gson = new Gson();
        LoanOfferDTO loanOfferDTO = gson.fromJson(loanOfferStr, LoanOfferDTO.class);

        applicationService.sendLoanOfferToDeal(loanOfferDTO);

        verify(serviceProxy).sendLoanOfferForCalculate(loanOfferDTO);
    }
}
