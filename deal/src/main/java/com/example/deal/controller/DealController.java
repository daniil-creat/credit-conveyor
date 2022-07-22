package com.example.deal.controller;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;
import com.example.deal.services.DealService;
import com.example.deal.services.EmailSevice;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/deal")
public class DealController {

    private final DealService dealService;
    private final EmailSevice emailSevice;

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
        dealService.createDocumentAndSendMessage(applicationId);
    }

    @PostMapping("document/{applicationId}/sign")
    public void signDocument(@PathVariable Long applicationId) {
        log.info("Start signDocument controller, applicationId: {}", applicationId);
        dealService.generateCodeAndSendMessage(applicationId);
    }

    @PostMapping("document/{applicationId}/code")
    public void signDocumentUser(@PathVariable Long applicationId, @RequestBody String code) {
        log.info("Start signDocument controller, applicationId: {}", applicationId);
        dealService.checkCodeAndSendAnswer(applicationId, code);
    }
}
