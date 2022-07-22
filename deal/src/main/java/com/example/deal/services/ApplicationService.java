package com.example.deal.services;

import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;
import com.example.deal.entity.Client;
import com.example.deal.entity.Credit;

public interface ApplicationService {

    Application creatApplication(Client client);

    Application findById(Long id);

    Application updateApplicationByLoanOffer(Application application, LoanOfferDTO loanOffer);

    Application updateByCredit(Credit credit , Long applicationId);

    Application update(Application application);

    void deletAll();
}
