package com.example.deal.controller;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.ScoringDataDTO;
import com.example.deal.proxy.ServiceProxy;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;
import com.example.deal.entity.Client;
import com.example.deal.services.ApplicationService;
import com.example.deal.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deal")
public class DealController {

    private final ClientService clientService;
    private final ApplicationService applicationService;
    private final ServiceProxy serviceProxy;

    @PostMapping("/application")
    public List<LoanOfferDTO> calculateLoanOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest) {
        Client client = clientService.creatClient(loanApplicationRequest);
        Application application = applicationService.creatApplication(client);
        List<LoanOfferDTO> loanOffers = serviceProxy.getLoanOffersOfConveyor(loanApplicationRequest);
        loanOffers.forEach(loanOffer -> loanOffer.setApplicationId(application.getId()));
        return loanOffers;
    }

    @PutMapping("/offer")
    public void calculateLoanOffers(@RequestBody LoanOfferDTO loanOffer) {
        Application application = applicationService.findById(loanOffer.getApplicationId());
        applicationService.updateApplicationByLoanOffer(application, loanOffer);
    }

    @PutMapping("/calculate/{applicationId}")
    public void calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequest, @PathVariable Long applicationId) {
        Application application = applicationService.findById(applicationId);
        Client client = application.getClient();
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .account(finishRegistrationRequest.getAccount())
                .amount(application.getAppliedOffer().getRequestedAmount())
                .birthdate(client.getBirthDate())
                .dependentAmount(finishRegistrationRequest.getDependentAmount())
                .employment(finishRegistrationRequest.getEmployment())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(finishRegistrationRequest.getGender())
                .isInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(application.getAppliedOffer().getIsSalaryClient())
                .maritalStatus(finishRegistrationRequest.getMaritalStatus())
                .passportIssueBranch(finishRegistrationRequest.getPassportIssueBranch())
                .passportIssueDate(finishRegistrationRequest.getPassportIssueDate())
                .passportNumber(client.getPassport().getNumber())
                .passportSeries(client.getPassport().getSeries())
                .term(application.getAppliedOffer().getTerm())
                .build();
        serviceProxy.getCredit(scoringDataDTO);
    }
}
