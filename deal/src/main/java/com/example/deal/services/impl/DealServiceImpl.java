package com.example.deal.services.impl;

import com.example.deal.dto.*;
import com.example.deal.entity.Application;
import com.example.deal.entity.Client;
import com.example.deal.entity.Credit;
import com.example.deal.proxy.ServiceProxy;
import com.example.deal.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DealServiceImpl implements DealService {

    private final ClientService clientService;
    private final ApplicationService applicationService;
    private final CreditService creditService;
    private final ServiceProxy serviceProxy;
    private final EmailSevice emailService;
    private final DocumentService documentService;
    private final EmailSevice emailSevice;

    @Override
    public List<LoanOfferDTO> calculatingLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        log.info("Service: Deal,calculatingLoanOffers method, parameters: {}", loanApplicationRequest);
        Client client = clientService.creatClient(loanApplicationRequest);
        Application application = applicationService.creatApplication(client);
        List<LoanOfferDTO> loanOffers = serviceProxy.getLoanOffersOfConveyor(loanApplicationRequest);
        loanOffers.forEach(loanOffer -> loanOffer.setApplicationId(application.getId()));
        log.info("Service: Deal, calculatingLoanOffers method, return: {}", loanOffers);
        return loanOffers;
    }

    @Override
    public Application calculatingLoanOffer(LoanOfferDTO loanOffer) {
        log.info("Service: Deal,calculatingLoanOffer method, parameters: {}", loanOffer);
        Application application = applicationService.findById(loanOffer.getApplicationId());
        Application updatedApplication = applicationService.updateApplicationByLoanOffer(application, loanOffer);
        log.info("Service: Deal,calculatingLoanOffer method, return: {}", updatedApplication);
        return updatedApplication;
    }

    @Override
    public void calculatingCredit(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId) {
        log.info("Service: Deal,calculatingCredit method, parameters: {}, {}", finishRegistrationRequest, applicationId);
        Application application = applicationService.findById(applicationId);
        Client client = application.getClient();
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .account(finishRegistrationRequest.getAccount())
                .amount(application.getAppliedOffer().getRequestedAmount())
                .birthdate(client.getBirthDate())
                .dependentAmount(finishRegistrationRequest.getDependentAmount())
                .employment(finishRegistrationRequest.getEmployment())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(finishRegistrationRequest.getGender())
                .isInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(application.getAppliedOffer().getIsSalaryClient())
                .maritalStatus(finishRegistrationRequest.getMaritalStatus())
                .passportIssueBranch(finishRegistrationRequest.getPassportIssueBranch())
                .passportIssueDate(finishRegistrationRequest.getPassportIssueDate())
                .passportNumber(client.getPassport().getNumber())
                .passportSeries(client.getPassport().getSeries())
                .term(application.getAppliedOffer().getTerm())
                .build();
        clientService.updateClient(scoringDataDTO, client.getId());
        CreditDTO creditDTO = serviceProxy.getCredit(scoringDataDTO);
        if (creditDTO == null) {
            log.info("Start send message about denied");
            emailService.sendMessageAboutDenied(applicationId);
        }
        Credit credit = creditService.saveCredit(creditDTO);
        Application updatedApplication = applicationService.updateByCredit(credit, applicationId);
        log.info("Service: Deal,calculatingCredit method, updatedApplication: {}", updatedApplication);
    }

    @Override
    public void createDocumentAndSendMessage(Long applicationId) throws IOException {
        log.info("Start method createDocumentAndSendMessage, Deal, parametr: {} ", applicationId);
        File fileClient = documentService.generetedDocumentAndGetFileClientInfo(applicationId);
        File fileCredit = documentService.generetedDocumentAndGetFileCreditInfo(applicationId);
        File filePayment = documentService.generetedDocumentAndGetFilePaymentInfo(applicationId);
        log.info("Start send message for send-documents");
        emailSevice.sendMessageForCreateDocuments(applicationId, fileClient, fileCredit, filePayment);
    }

    @Override
    public void generateCodeAndSendMessage(Long applicationId) {
        log.info("Service: Deal,generateCodeAndSendMessage method, parameters: {}", applicationId);
        int max = 9999;
        int min = 1000;
        Integer code = (int) (Math.random() * ++max) + min;
        Application application = applicationService.findById(applicationId);
        application.setSesCode(code.toString());
        applicationService.update(application);
        log.info("Start send message for send-sign");
        emailSevice.sendMessageWithCode(applicationId, code.toString());
    }

    @Override
    public void checkCodeAndSendAnswer(Long applicationId, String code) {
        log.info("Service: Deal,checkCodeAndSendAnswer method, parameters: {},{}", code, applicationId);
        boolean answer = false;
        Application application = applicationService.findById(applicationId);
        if (application.getSesCode().equals(code)) {
            answer = true;
        } else {
            answer = false;
        }
        log.info("Start send message for credit-issued");
        emailSevice.sendMessageAboutAnswer(answer, applicationId);
    }
}
