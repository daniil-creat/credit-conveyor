package com.example.conveyor.services;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.ScoringDataDTO;

public interface ScoringServcie {

    CreditDTO getCredit(ScoringDataDTO scoringData);
}
