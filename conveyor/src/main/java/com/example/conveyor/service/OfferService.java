package com.example.conveyor.service;

import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;

import java.util.List;

public interface OfferService {

    List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequest);

}
