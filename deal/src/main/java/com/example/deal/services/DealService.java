package com.example.deal.services;


import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;

import java.util.List;

public interface DealService {
    List<LoanOfferDTO> calculatingLoanOffers(LoanApplicationRequestDTO loanApplicationRequest);
    Application calculatingLoanOffer(LoanOfferDTO loanOffer);
    void calculatingCredit(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId);
    String generateCode(Long applicationId);
    Boolean checkCode(String code, Long id);
}
