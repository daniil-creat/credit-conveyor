package com.example.conveyor.service.impl;

import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;
import com.example.conveyor.service.OfferService;
import com.example.conveyor.service.ScoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final ScoringService scoringService;

    @Override
    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
            List<LoanOfferDTO> listLoanOfferDto = new ArrayList<>();
            if (scoringService.validationAge(loanApplicationRequest.getBirthday())) {
                listLoanOfferDto.add(calculatingLoanOffer(false, false, loanApplicationRequest, 1L));
                listLoanOfferDto.add(calculatingLoanOffer(false, true, loanApplicationRequest, 2L));
                listLoanOfferDto.add(calculatingLoanOffer(true, false, loanApplicationRequest, 3L));
                listLoanOfferDto.add(calculatingLoanOffer(true, true, loanApplicationRequest, 4L));
            }
            return listLoanOfferDto;
    }

    private LoanOfferDTO calculatingLoanOffer(boolean isInsuranceEnabled, boolean isSalaryClient, LoanApplicationRequestDTO loanApplicationRequest, Long id) {
        BigDecimal rate = scoringService.calculatingBaseRate(isInsuranceEnabled, isSalaryClient);
        BigDecimal requestedAmount = loanApplicationRequest.getAmount();
        requestedAmount = isInsuranceEnabled ? requestedAmount.add(BigDecimal.valueOf(1000)) : requestedAmount;
        BigDecimal totalAmount = scoringService.calculatingTotalAmount(rate, requestedAmount, loanApplicationRequest.getTerm());
        log.info("Get loan offers: total amount = {}, rate = {}", totalAmount, rate);
        BigDecimal monthlyPayment = scoringService.calculatingMonthlyPayment(rate, requestedAmount, loanApplicationRequest.getTerm());
        return LoanOfferDTO.builder().applicationId(id)
                .rate(rate)
                .requestedAmount(loanApplicationRequest.getAmount())
                .totalAmount(totalAmount)
                .term(loanApplicationRequest.getTerm())
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .monthlyPayment(monthlyPayment)
                .build();
    }
}
