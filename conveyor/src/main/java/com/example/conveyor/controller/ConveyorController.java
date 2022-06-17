package com.example.conveyor.controller;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;
import com.example.conveyor.dto.ScoringDataDTO;
import com.example.conveyor.services.PrescoringService;
import com.example.conveyor.services.ScoringServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/conveyor")
public class ConveyorController {

    @Autowired
    private ScoringServcie scoringService;
    @Autowired
    private PrescoringService prescoringService;

    @PostMapping("/offers")
    public List<LoanOfferDTO> differentOffersOfLoanConditions(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO) throws Exception {
        return prescoringService.getLoanOffers(loanApplicationRequestDTO);
    }

    @PostMapping("/calculation")
    public CreditDTO calculationOfFullLoanParameters(@RequestBody @Valid ScoringDataDTO scoringDataDTO) throws Exception {
        return scoringService.getCredit(scoringDataDTO);
    }
}
