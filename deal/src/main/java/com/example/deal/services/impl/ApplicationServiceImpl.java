package com.example.deal.services.impl;

import com.example.deal.dto.ApplicationStatusHistoryDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;
import com.example.deal.entity.Client;
import com.example.deal.enums.Status;
import com.example.deal.repository.ApplicationRepository;
import com.example.deal.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    public Application creatApplication(Client client) {
        Application application = Application.builder()
                .client(client)
                .creationDate(LocalDate.now())
                .build();
        return applicationRepository.save(application);
    }

    @Override
    public Application findById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

    @Override
    public Application updateApplicationByLoanOffer(Application application, LoanOfferDTO loanOffer) {
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
        return applicationRepository.save(application);
    }

    @Override
    public void deletAll() {
        applicationRepository.deleteAll();
    }
}
