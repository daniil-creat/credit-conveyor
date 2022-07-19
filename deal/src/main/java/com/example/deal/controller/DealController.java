package com.example.deal.controller;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.services.DealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/deal")
public class DealController {

    private final DealService dealService;


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
        dealService.calculatingLoanOffer(loanOffer);
    }

    @PutMapping("/calculate/{applicationId}")
    public void calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequest, @PathVariable Long applicationId) {
        log.info("Request finishRegistrationRequest: {}, applicationId: {}", finishRegistrationRequest, applicationId);
        dealService.calculatingCredit(finishRegistrationRequest, applicationId);
    }
}
