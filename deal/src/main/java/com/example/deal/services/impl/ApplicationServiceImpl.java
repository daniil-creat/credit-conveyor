package com.example.deal.services.impl;

import com.example.deal.dto.ApplicationStatusHistoryDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;
import com.example.deal.entity.Client;
import com.example.deal.entity.Credit;
import com.example.deal.enums.Status;
import com.example.deal.repository.ApplicationRepository;
import com.example.deal.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    public Application creatApplication(Client client) {
        log.info("Service: Application, creatApplication method, parameters:{}", client);
        Application application = Application.builder()
                .client(client)
                .creationDate(LocalDate.now())
                .build();
        Application savedApplication = applicationRepository.save(application);
        log.info("Service: Application, creatApplication method, return:{}", savedApplication);
        return savedApplication;
    }

    @Override
    public Application findById(Long id) {
        log.info("Service: Application, findById method, parameters:{}", id);
        Application application = applicationRepository.findById(id).orElse(null);
        log.info("Service: Application, findById method, return:{}", application);
        return application;
    }

    @Override
    public Application updateApplicationByLoanOffer(Application application, LoanOfferDTO loanOffer) {
        log.info("Service: Application, updateApplicationByLoanOffer method, parameters:{}, {}", application, loanOffer);
        List<ApplicationStatusHistoryDTO> listStatus = new ArrayList<>();
        application.setAppliedOffer(LoanOfferDTO.builder()
                .applicationId(loanOffer.getApplicationId())
                .term(loanOffer.getTerm())
                .isInsuranceEnabled(loanOffer.getIsInsuranceEnabled())
                .isSalaryClient(loanOffer.getIsSalaryClient())
                .monthlyPayment(loanOffer.getMonthlyPayment())
                .totalAmount(loanOffer.getTotalAmount())
                .requestedAmount(loanOffer.getRequestedAmount())
                .rate(loanOffer.getRate())
                .build());
        ApplicationStatusHistoryDTO statusHistory = ApplicationStatusHistoryDTO.builder()
                .changeType(Status.PREAPPROVAL)
                .localDateTime(LocalDate.now())
                .build();
        listStatus.add(statusHistory);
        application.setStatusHistory(listStatus);
        application.setStatus(Status.PREAPPROVAL);

        Application savedApplication = applicationRepository.save(application);
        log.info("Service: Application, updateApplicationByLoanOffer method, return:{}", savedApplication);
        return savedApplication;
    }

    @Override
    public Application updateByCredit(Credit credit, Long id) {
        log.info("Service: Application, updateApplicationByClient method, return:{}", credit);
        Application application = applicationRepository.findById(id).get();
        application.setCredit(credit);
        return applicationRepository.save(application);
    }

    @Override
    public Application update(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    public void deletAll() {
        log.info("Service: Application, deleteAll method");
        applicationRepository.deleteAll();
    }
}
