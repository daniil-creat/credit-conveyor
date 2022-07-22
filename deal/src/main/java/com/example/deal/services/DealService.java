package com.example.deal.services;


import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;

import java.io.IOException;
import java.util.List;

public interface DealService {
    List<LoanOfferDTO> calculatingLoanOffers(LoanApplicationRequestDTO loanApplicationRequest);
    Application calculatingLoanOffer(LoanOfferDTO loanOffer);
    void calculatingCredit(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId);
    void createDocumentAndSendMessage(Long id) throws IOException;
    void generateCodeAndSendMessage(Long id);
    void checkCodeAndSendAnswer(Long applicationId, String code);
}
