package com.example.deal.services.impl;

import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.Passport;
import com.example.deal.entity.Client;
import com.example.deal.repository.ClientRepository;
import com.example.deal.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client creatClient(LoanApplicationRequestDTO loanApplicationRequest) {
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

       return clientRepository.save(client);
    }

    @Override
    public void deleteAll() {
        clientRepository.deleteAll();
    }
}
