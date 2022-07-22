package com.example.deal.services.impl;

import com.example.deal.dto.*;
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

    @Override
    public Client updateClient(ScoringDataDTO scoringData, Long id) {
        log.info("Service: Client, updateClient method, parameters:{}, {}", scoringData, id);
        Passport passport = Passport.builder()
                .series(scoringData.getPassportSeries())
                .number(scoringData.getPassportNumber())
                .issueBranch(scoringData.getPassportIssueBranch())
                .issueDate(scoringData.getPassportIssueDate())
                .build();
        EmploymentDTO employment = EmploymentDTO.builder()
                .employmentStatus(scoringData.getEmployment().getEmploymentStatus())
                .employerINN(scoringData.getEmployment().getEmployerINN())
                .position(scoringData.getEmployment().getPosition())
                .salary(scoringData.getEmployment().getSalary())
                .workExperienceTotal(scoringData.getEmployment().getWorkExperienceTotal())
                .workExperienceCurrent(scoringData.getEmployment().getWorkExperienceCurrent())
                .build();
        Client client = clientRepository.findById(id).get();
        client.setEmployment(employment);
        client.setDependentAmount(scoringData.getDependentAmount());
        client.setPassport(passport);
        Client clientSaved = clientRepository.save(client);
        log.info("Service: Client, updateClient method, return:{}", clientSaved);
        return clientSaved;
    }
}
