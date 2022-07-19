package com.example.deal;

import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.dto.Passport;
import com.example.deal.entity.Application;
import com.example.deal.entity.Client;
import com.example.deal.enums.Status;
import com.example.deal.repository.ClientRepository;
import com.example.deal.services.ApplicationService;
import com.example.deal.services.ClientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application.properties")
class ApplicationServiceTest {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    Passport passport = null;
    Client client = null;

    @AfterEach
    void cleanup() {
        applicationService.deletAll();
        clientService.deleteAll();
    }

    @BeforeEach
    void setup() {
        passport = Passport.builder()
                .series("4016")
                .number("414201")
                .build();

        client = clientRepository.save(Client.builder().firstName("Дима")
                .lastName("Кузнецов")
                .middleName("Валерьевич")
                .email("fff@fff.com")
                .birthDate(LocalDate.of(1996, 3, 8))
                .passport(passport)
                .build());
    }


    @Test
    void creatApplicationTest() {
        Application application = applicationService.creatApplication(client);

        assertEquals(application.getClient().getFirstName(), client.getFirstName());
        assertEquals(application.getClient().getEmail(), client.getEmail());
        assertEquals(application.getClient().getLastName(), client.getLastName());
        assertEquals(application.getClient().getMiddleName(), client.getMiddleName());
        assertEquals(application.getClient().getPassport().getSeries(), client.getPassport().getSeries());
        assertEquals(application.getClient().getPassport().getNumber(), client.getPassport().getNumber());
    }

    @Test
    void findByIdTest() {
        Application application = applicationService.creatApplication(client);
        Application applicationOfDb = applicationService.findById(application.getId());

        assertEquals(applicationOfDb.getClient().getFirstName(), client.getFirstName());
        assertEquals(applicationOfDb.getClient().getEmail(), client.getEmail());
        assertEquals(applicationOfDb.getClient().getLastName(), client.getLastName());
        assertEquals(applicationOfDb.getClient().getMiddleName(), client.getMiddleName());
        assertEquals(applicationOfDb.getClient().getPassport().getSeries(), client.getPassport().getSeries());
        assertEquals(applicationOfDb.getClient().getPassport().getNumber(), client.getPassport().getNumber());
    }

    @Test
    void updateApplicationByLoanOfferTest() {
        Application application = applicationService.creatApplication(client);

        LoanOfferDTO loanOffer = LoanOfferDTO.builder()
                .applicationId(application.getId())
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .term(6)
                .monthlyPayment(BigDecimal.valueOf(100))
                .rate(BigDecimal.valueOf(12))
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        Application newApplication = applicationService.updateApplicationByLoanOffer(application, loanOffer);

        assertEquals(newApplication.getAppliedOffer().getIsInsuranceEnabled(), loanOffer.getIsInsuranceEnabled());
        assertEquals(newApplication.getAppliedOffer().getIsSalaryClient(), loanOffer.getIsSalaryClient());
        assertEquals(newApplication.getAppliedOffer().getTerm(), loanOffer.getTerm());
        assertEquals(newApplication.getAppliedOffer().getRequestedAmount(), loanOffer.getRequestedAmount());
        assertEquals(newApplication.getAppliedOffer().getMonthlyPayment(), loanOffer.getMonthlyPayment());
        assertEquals(newApplication.getStatus(), Status.PREAPPROVAL);
        assertEquals(newApplication.getStatusHistory().size(), 1);

    }

    @Test
    void deleteAllTest() {
        Client clientTwo = clientRepository.save(Client.builder().firstName("Дима")
                .lastName("Васин")
                .middleName("Валерьевич")
                .email("fff@fff.com")
                .birthDate(LocalDate.of(1996, 3, 8))
                .passport(passport)
                .build());

        applicationService.creatApplication(client);
        applicationService.creatApplication(clientTwo);
        applicationService.deletAll();
        Application application = applicationService.findById(1L);

        assertEquals(application, null);


    }

}
