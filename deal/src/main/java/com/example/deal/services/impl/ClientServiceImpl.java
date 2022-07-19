package com.example.deal.services.impl;

import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.Passport;
import com.example.deal.entity.Client;
import com.example.deal.repository.ClientRepository;
import com.example.deal.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log4j2
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client creatClient(LoanApplicationRequestDTO loanApplicationRequest) {
        log.info("Service: Client, creatClient method, parameters:{}", loanApplicationRequest);
        Passport passport = Passport.builder()
                .series(loanApplicationRequest.getPassportSeries())
                .number(loanApplicationRequest.getPassportNumber())
                .build();

        Client client = Client.builder().firstName(loanApplicationRequest.getFirstName())
                .lastName(loanApplicationRequest.getLastName())
                .middleName(loanApplicationRequest.getMiddleName())
                .email(loanApplicationRequest.getEmail())
                .birthDate(loanApplicationRequest.getBirthday())
                .passport(passport)
                .build();

        Client savedClient = clientRepository.save(client);
        log.info("Service: Client, creatClient method, return:{}", savedClient);
       return savedClient;
    }

    @Override
    public void deleteAll() {
        log.info("Service: Client, deleteAll method");
        clientRepository.deleteAll();
    }
}
