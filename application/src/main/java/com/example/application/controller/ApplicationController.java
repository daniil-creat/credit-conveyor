package com.example.application.controller;

import com.example.application.dto.LoanApplicationRequestDTO;
import com.example.application.dto.LoanOfferDTO;
import com.example.application.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public List<LoanOfferDTO> prescoringAndGetLoanOffers(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequest){
        log.info("Request loanApplicationRequest: {}", loanApplicationRequest);
        List<LoanOfferDTO> loanOffers = applicationService.getLoanOffers(loanApplicationRequest);
        log.info("Response loanOffers: {}", loanOffers);
        return loanOffers;
    }

    @PostMapping("/offer")
    public void sendLoanOfferToDealForCalculate(@RequestBody LoanOfferDTO loanOffer){
        log.info("Request loanOffer: {}",loanOffer);
        applicationService.sendLoanOfferToDeal(loanOffer);
    }

}
