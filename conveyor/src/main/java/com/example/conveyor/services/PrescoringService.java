package com.example.conveyor.services;

import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;

import java.util.List;

public interface PrescoringService {

    /**
     * Calculating loanOffers
     *
     * @param loanApplicationRequest
     * @return creditDTO
     */
    List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequest);
}
