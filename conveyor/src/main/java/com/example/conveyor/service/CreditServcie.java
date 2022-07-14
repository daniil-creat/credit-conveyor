package com.example.conveyor.service;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.ScoringDataDTO;

public interface CreditServcie {

    CreditDTO getCredit(ScoringDataDTO scoringData);

}
