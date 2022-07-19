package com.example.deal.services;

import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;
import com.example.deal.entity.Client;

public interface ApplicationService {

    Application creatApplication(Client client);

    Application findById(Long id);

    Application updateApplicationByLoanOffer(Application application, LoanOfferDTO loanOffer);

    void deletAll();
}
