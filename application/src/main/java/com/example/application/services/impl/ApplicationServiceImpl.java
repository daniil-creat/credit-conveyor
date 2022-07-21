package com.example.application.services.impl;

import com.example.application.dto.LoanApplicationRequestDTO;
import com.example.application.dto.LoanOfferDTO;
import com.example.application.proxy.ServiceProxy;
import com.example.application.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ServiceProxy serviceProxy;

    @Override
    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        log.info("Service: Application, applicationService, getLoanOffers method, parameters: {}", loanApplicationRequest);
        List<LoanOfferDTO> loanOffers = serviceProxy.createApplicationAndGetLoanOffers(loanApplicationRequest);
        log.info("Service: Application, applicationService, getLoanOffers method, return: {}", loanOffers);
        return loanOffers;
    }

    @Override
    public void sendLoanOfferToDeal(LoanOfferDTO loanOffer) {
        log.info("Service: Application, applicationService, sendLoanOfferToDeal method, parameters: {}", loanOffer);
        serviceProxy.sendLoanOfferForCalculate(loanOffer);
    }
}
