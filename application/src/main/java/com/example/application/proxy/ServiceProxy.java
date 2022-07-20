package com.example.deal.proxy;

import com.example.deal.dto.CreditDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "conveyor", url = "localhost:8081")
public interface ServiceProxy {
    @PostMapping("/conveyor/offers")
    List<LoanOfferDTO> getLoanOffersOfConveyor(LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/conveyor/calculation")
    CreditDTO getCredit(ScoringDataDTO scoringDataDTO);
}
