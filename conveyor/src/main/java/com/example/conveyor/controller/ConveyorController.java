package com.example.conveyor.controller;

import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;
import com.example.conveyor.dto.ScoringDataDTO;
import com.example.conveyor.service.OfferService;
import com.example.conveyor.service.CreditServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conveyor")
public class ConveyorController {

    private final CreditServcie creditService;

    private final OfferService offerService;

    @PostMapping("/offers")
    public List<LoanOfferDTO> differentOffersOfLoanConditions(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return offerService.getLoanOffers(loanApplicationRequestDTO);
    }

    @PostMapping("/calculation")
    public CreditDTO calculationOfFullLoanParameters(@RequestBody @Valid ScoringDataDTO scoringDataDTO) {
        return creditService.getCredit(scoringDataDTO);
    }
}
