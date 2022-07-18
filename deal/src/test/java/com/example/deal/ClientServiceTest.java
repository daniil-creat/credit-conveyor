package com.example.deal;

import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.entity.Client;
import com.example.deal.repository.ClientRepository;
import com.example.deal.services.ClientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    LoanApplicationRequestDTO loanApplicationRequest = null;

    @BeforeEach
    void setup() {
        loanApplicationRequest = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(10))
                .birthday(LocalDate.of(1996, 5, 5))
                .firstName("Dima")
                .lastName("Dima")
                .middleName("Dima")
                .term(6)
                .passportNumber("12341234")
                .build();
    }

    @AfterEach
    void cleanup() {
        clientService.deleteAll();
    }

    @Test
    void creatClientTest() {
        LoanApplicationRequestDTO loanApplicationRequestTwo = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(11))
                .birthday(LocalDate.of(1996, 5, 5))
                .firstName("Dima")
                .lastName("Dima")
                .middleName("Dima")
                .term(6)
                .passportNumber("12341234")
                .build();

        clientService.creatClient(loanApplicationRequest);
        clientService.creatClient(loanApplicationRequestTwo);

        clientService.deleteAll();

        assertTrue(clientRepository.findById(1L).isEmpty());
    }

    @Test
    void deleteAllTest() {
        Client client = clientService.creatClient(loanApplicationRequest);

        assertEquals(client.getLastName(), loanApplicationRequest.getLastName());
        assertEquals(client.getMiddleName(), loanApplicationRequest.getMiddleName());
        assertEquals(client.getBirthDate(), loanApplicationRequest.getBirthday());
        assertEquals(client.getPassport().getNumber(), loanApplicationRequest.getPassportNumber());
    }

}
