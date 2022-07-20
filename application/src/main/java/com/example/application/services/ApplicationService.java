package com.example.application.services;

import com.example.application.dto.LoanApplicationRequestDTO;
import com.example.application.dto.LoanOfferDTO;

import java.util.List;

public interface ApplicationService {
    List<LoanOfferDTO>getLoanOffers(LoanApplicationRequestDTO loanApplicationRequest);

    void sendLoanOfferToDeal(LoanOfferDTO loanOffer);
}
