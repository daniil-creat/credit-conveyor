package com.example.application;

import com.example.application.dto.LoanApplicationRequestDTO;
import com.example.application.dto.LoanOfferDTO;
import com.example.application.services.ApplicationService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ApplicationService applicationService;

    @Test
    void prescoringAndGetLoanOffersTest() throws Exception {
        File file = ResourceUtils.getFile("classpath:LoanApplicationRequest.json");
        String loanApplicationRequestStr = new String(Files.readAllBytes(file.toPath()));

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

        LoanOfferDTO loanOffer = LoanOfferDTO.builder()
                .requestedAmount(BigDecimal.valueOf(100000))
                .isSalaryClient(true)
                .isInsuranceEnabled(true)
                .term(6)
                .build();

        List<LoanOfferDTO> list = List.of(loanOffer, loanOffer, loanOffer, loanOffer);

        when(applicationService.getLoanOffers(any())).thenReturn(list);

        this.mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON).content(loanApplicationRequestStr))
                .andExpect(status().isOk())
                .andReturn();

        verify(applicationService).getLoanOffers(loanApplicationRequest);
    }

    @Test
    void prescoringAndGetLoanOffersWrongEmailTest() throws Exception {
        File file = ResourceUtils.getFile("classpath:LoanApplicationRequestWrongEmail.json");
        String loanApplicationRequestStr = new String(Files.readAllBytes(file.toPath()));

        this.mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON).content(loanApplicationRequestStr))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error validation, field email, must be [from 2 to 50 characters]@[from 2 to 30 characters]"));

        verifyNoInteractions(applicationService);
    }

    @Test
    void prescoringAndGetLoanOffersWrongTermTest() throws Exception {
        File file = ResourceUtils.getFile("classpath:LoanApplicationRequestWrongTerm.json");
        String loanApplicationRequestStr = new String(Files.readAllBytes(file.toPath()));

        this.mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON).content(loanApplicationRequestStr))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error validation, field term, term must be 6 and more months"));

        verifyNoInteractions(applicationService);
    }

    @Test
    void prescoringAndGetLoanOffersWrongAmountTest() throws Exception {
        File file = ResourceUtils.getFile("classpath:LoanApplicationRequestWrongAmount.json");
        String loanApplicationRequestStr = new String(Files.readAllBytes(file.toPath()));

        this.mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON).content(loanApplicationRequestStr))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error validation, field amount, amount must be 100000 and more"));

        verifyNoInteractions(applicationService);
    }

    @Test
    void prescoringAndGetLoanOffersWrongPassportDataTest() throws Exception {
        File file = ResourceUtils.getFile("classpath:LoanApplicationRequestWrongPassportData.json");
        String loanApplicationRequestStr = new String(Files.readAllBytes(file.toPath()));

        this.mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON).content(loanApplicationRequestStr))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error validation, field passportSeries, must be 4 digits"));

        verifyNoInteractions(applicationService);
    }

    @Test
    void prescoringAndGetLoanOffersWrongBirthDayTest() throws Exception {
        File file = ResourceUtils.getFile("classpath:LoanApplicationRequestWrongBirthDay.json");
        String loanApplicationRequestStr = new String(Files.readAllBytes(file.toPath()));

        this.mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON).content(loanApplicationRequestStr))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(applicationService);
    }

    @Test
    void prescoringAndGetLoanOffersWrongName() throws Exception {
        File file = ResourceUtils.getFile("classpath:LoanApplicationRequestWrongName.json");
        String loanApplicationRequestStr = new String(Files.readAllBytes(file.toPath()));

        this.mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON).content(loanApplicationRequestStr))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error validation, field firstName, must be Latin letters"));

        verifyNoInteractions(applicationService);
    }

    @Test
    void sendLoanOfferToDealForCalculateTest() throws Exception {
        File file = ResourceUtils.getFile("classpath:loanOffer.json");
        String loanOfferStr = new String(Files.readAllBytes(file.toPath()));

        Gson gson = new Gson();
        LoanOfferDTO loanOffer = gson.fromJson(loanOfferStr, LoanOfferDTO.class);

        this.mockMvc.perform(post("/application/offer")
                        .contentType(MediaType.APPLICATION_JSON).content(loanOfferStr))
                .andExpect(status().isOk());

        verify(applicationService).sendLoanOfferToDeal(loanOffer);
    }
}
