package com.example.conveyor.controller;

import com.example.conveyor.dto.*;
import com.example.conveyor.services.serviceImpl.PrescoringServiceImpl;
import com.example.conveyor.services.serviceImpl.ScoringServiceImpl;
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
    private ScoringServiceImpl scoringService;
    @Autowired
    private PrescoringServiceImpl prescoringService;

    @PostMapping("/offers")
    public Wrapper<List<LoanOfferDTO>> differentOffersOfLoanConditions(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO) throws Exception {
        return new Wrapper<>(prescoringService.getLoanOffers(loanApplicationRequestDTO));
    }

    @PostMapping("/calculation")
    public Wrapper<CreditDTO> calculationOfFullLoanParameters(@RequestBody @Valid ScoringDataDTO scoringDataDTO) throws Exception {
        return new Wrapper<>(scoringService.getCredit(scoringDataDTO));
    }
}
