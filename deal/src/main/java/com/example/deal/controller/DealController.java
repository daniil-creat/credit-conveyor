package com.example.deal.controller;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;
import com.example.deal.services.DealService;
import com.example.deal.services.DocumentService;
import com.example.deal.services.EmailSevice;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/deal")
public class DealController {

    private final DealService dealService;
    private final EmailSevice emailSevice;
    private final DocumentService documentService;

    @PostMapping("/application")
    public List<LoanOfferDTO> calculateLoanOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest) {
        log.info("Request loanApplicationRequest: {}", loanApplicationRequest);
        List<LoanOfferDTO> loanOffers = dealService.calculatingLoanOffers(loanApplicationRequest);
        log.info("Response loanOffers: {}", loanOffers);
        return loanOffers;
    }

    @PutMapping("/offer")
    public void calculateLoanOffer(@RequestBody LoanOfferDTO loanOffer) {
        log.info("Request loanOffer: {}", loanOffer);
        Application application = dealService.calculatingLoanOffer(loanOffer);
        log.info("Start send message for finish-registration");
        emailSevice.sendMessageForFinishRegistration(application);
    }

    @PutMapping("/calculate/{applicationId}")
    public void calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequest, @PathVariable Long applicationId) {
        log.info("Request finishRegistrationRequest: {}, applicationId: {}", finishRegistrationRequest, applicationId);
        dealService.calculatingCredit(finishRegistrationRequest, applicationId);
        log.info("Start send message for create-documents");
        emailSevice.sendMessageForFinishRegistrationById(applicationId);
    }

    @PostMapping("document/{applicationId}/send")
    public void getDocument(@PathVariable Long applicationId) throws IOException {
        log.info("Start getDocument controller, applicationId: {}", applicationId);
        File fileClient = documentService.generetedDocumentAndGetFileClientInfo(applicationId);
        File fileCredit = documentService.generetedDocumentAndGetFileCreditInfo(applicationId);
        File filePayment = documentService.generetedDocumentAndGetFilePaymentInfo(applicationId);
        log.info("Start send message for send-documents");
        emailSevice.sendMessageForCreateDocuments(applicationId, fileClient, fileCredit, filePayment);
    }

    @PostMapping("document/{applicationId}/sign")
    public void signDocument(@PathVariable Long applicationId) throws IOException {
        log.info("Start signDocument controller, applicationId: {}", applicationId);
        String code = dealService.generateCode(applicationId);
        log.info("Start send message for send-sign");
        emailSevice.sendMessageWithCode(applicationId, code);
    }

    @PostMapping("document/{applicationId}/code")
    public void signDocumentUser(@PathVariable Long applicationId, @RequestBody String code) throws IOException {
        log.info("Start signDocument controller, applicationId: {}", applicationId);
        Boolean answer = dealService.checkCode(code, applicationId);
        log.info("Start send message for credit-issued");
        emailSevice.sendMessageAboutAnswer(answer, applicationId);
    }
}
