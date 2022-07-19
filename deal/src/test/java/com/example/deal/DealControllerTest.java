package com.example.deal;

import com.example.deal.dto.*;
import com.example.deal.entity.Application;
import com.example.deal.entity.Client;
import com.example.deal.enums.EmploymentStatus;
import com.example.deal.enums.Gender;
import com.example.deal.enums.MaritalStatus;
import com.example.deal.enums.Position;
import com.example.deal.proxy.ServiceProxy;
import com.example.deal.services.ApplicationService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ApplicationService applicationService;
    @MockBean
    private Application application;
    @MockBean
    private ServiceProxy serviceProxy;
    @MockBean
    private CreditDTO creditDTO;
    @MockBean
    private LoanOfferDTO loanOfferDTO;

    @Test
    void calculateLoanOffersTest() throws Exception {
        File file = ResourceUtils.getFile("classpath:LoanApplicationRequest.json");
        String loanApplicationRequestStr = new String(Files.readAllBytes(file.toPath()));

        File fileTwo = ResourceUtils.getFile("classpath:LoanOffers.json");
        String loanOffers = new String(Files.readAllBytes(fileTwo.toPath()));

        LoanOfferDTO loanOfferDTO1 = new LoanOfferDTO();
        LoanOfferDTO loanOfferDTO2 = new LoanOfferDTO();
        LoanOfferDTO loanOfferDTO3 = new LoanOfferDTO();
        LoanOfferDTO loanOfferDTO4 = new LoanOfferDTO();

        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
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

        when(applicationService.creatApplication(any())).thenReturn(application);
        when(application.getId()).thenReturn(1L);
        when(serviceProxy.getLoanOffersOfConveyor(any())).thenReturn(List.of(loanOfferDTO1, loanOfferDTO2, loanOfferDTO3, loanOfferDTO4));

        this.mockMvc.perform(post("/deal/application")
                        .contentType(MediaType.APPLICATION_JSON).content(loanApplicationRequestStr))
                .andExpect(status().isOk())
                .andReturn();

        verify(serviceProxy).getLoanOffersOfConveyor(loanApplicationRequestDTO);
    }

    @Test
    void calculateLoanOfferTest() throws Exception {
        File file = ResourceUtils.getFile("classpath:loanOffer.json");
        String loanOffer = new String(Files.readAllBytes(file.toPath()));

        when(applicationService.findById(anyLong())).thenReturn(application);
        when(applicationService.updateApplicationByLoanOffer(any(), any()))
                .thenReturn(application);
        when(serviceProxy.getLoanOffersOfConveyor(any())).thenReturn(List.of(loanOfferDTO));

        this.mockMvc.perform(put("/deal/offer")
                        .contentType(MediaType.APPLICATION_JSON).content(loanOffer))
                .andExpect(status().isOk())
                .andReturn();

        verify(applicationService).findById(1L);
    }

    @Test
    void calculateCreditTest() throws Exception {
        File file = ResourceUtils.getFile("classpath:finishRegistrationRequest.json");
        String finishReg = new String(Files.readAllBytes(file.toPath()));

        LoanOfferDTO loanOffer = LoanOfferDTO.builder()
                .requestedAmount(BigDecimal.valueOf(100000))
                .isSalaryClient(true)
                .isInsuranceEnabled(true)
                .term(6)
                .build();
        Client client = Client.builder()
                .birthDate(LocalDate.of(1996, 10, 8))
                .firstName("fff")
                .lastName("dfds")
                .middleName("sgfrg")
                .passport(Passport.builder().number("231341")
                        .series("1342")
                        .build())
                .build();

        Application applicationForUpdate = Application.builder()
                .appliedOffer(loanOffer)
                .client(client)
                .build();

        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .employerINN("312434134")
                .position(Position.WORKER)
                .workExperienceTotal(2)
                .workExperienceCurrent(6)
                .salary(BigDecimal.valueOf(100000)).build();

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .account("123224214")
                .amount(BigDecimal.valueOf(100000))
                .birthdate(LocalDate.of(1996, 10, 8))
                .dependentAmount(0)
                .employment(employmentDTO)
                .firstName("fff")
                .lastName("dfds")
                .middleName("sgfrg")
                .gender(Gender.MALE)
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .maritalStatus(MaritalStatus.MARRIED)
                .passportIssueBranch("124124")
                .passportIssueDate(LocalDate.of(1996, 9, 3))
                .passportNumber("231341")
                .passportSeries("1342")
                .term(6)
                .build();

        when(applicationService.findById(anyLong())).thenReturn(applicationForUpdate);
        when(serviceProxy.getCredit(any())).thenReturn(creditDTO);


        this.mockMvc.perform(put("/deal/calculate/1")
                        .contentType(MediaType.APPLICATION_JSON).content(finishReg))
                .andExpect(status().isOk())
                .andReturn();

        verify(applicationService).findById(1L);
        verify(serviceProxy).getCredit(scoringDataDTO);
    }
}
