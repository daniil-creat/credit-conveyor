package com.example.deal.services;


import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;

import java.util.List;

public interface DealService {
    List<LoanOfferDTO> calculatingLoanOffers(LoanApplicationRequestDTO loanApplicationRequest);
    void calculatingLoanOffer(LoanOfferDTO loanOffer);
    void calculatingCredit(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId);
}
