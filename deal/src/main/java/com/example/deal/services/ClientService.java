package com.example.deal.services;

import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.entity.Client;

public interface ClientService {
    Client creatClient(LoanApplicationRequestDTO loanApplicationRequest);

    void deleteAll();

}
