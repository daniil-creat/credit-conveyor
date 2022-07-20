package com.example.application.proxy;

import com.example.application.dto.LoanApplicationRequestDTO;
import com.example.application.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "conveyor", url = "localhost:8082")
public interface ServiceProxy {
    @PostMapping("/deal/application")
    List<LoanOfferDTO> createApplicationAndGetLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PutMapping("/deal/offer")
    void sendLoanOfferForCalculate(LoanOfferDTO loanOfferDTO);
}
