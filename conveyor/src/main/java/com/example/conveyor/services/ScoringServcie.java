package com.example.conveyor.services;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.ScoringDataDTO;

public interface ScoringServcie {

    /**
     * Calculating credit
     * @param scoringData
     * @return creditDTO
     */
    CreditDTO getCredit(ScoringDataDTO scoringData);
}
