package com.example.conveyor.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LoanOfferDTO {

    private LoanOfferDTO(LoanOfferDtoBuilder builder) {
        applicationId = builder.applicationId;
        totalAmount = builder.totalAmount;
        term = builder.term;
        requestedAmount = builder.requestedAmount;
        monthlyPayment = builder.monthlyPayment;
        rate = builder.rate;
        isInsuranceEnabled = builder.isInsuranceEnabled;
        isSalaryClient = builder.isSalaryClient;
    }

    private Long applicationId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

    public static class LoanOfferDtoBuilder {
        private BigDecimal totalAmount;
        private Long applicationId;
        private Integer term;
        private BigDecimal requestedAmount;
        private BigDecimal monthlyPayment;
        private BigDecimal rate;
        private Boolean isInsuranceEnabled;
        private Boolean isSalaryClient;

        public LoanOfferDtoBuilder(){}

        public LoanOfferDtoBuilder setRate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }
        public LoanOfferDtoBuilder setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public LoanOfferDtoBuilder setTerm(Integer term) {
            this.term = term;
            return this;
        }

        public LoanOfferDtoBuilder setApplicationId(Long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public LoanOfferDtoBuilder setRequestedAmount(BigDecimal requestedAmount) {
            this.requestedAmount = requestedAmount;
            return this;
        }

        public LoanOfferDtoBuilder setMonthlyPayment(BigDecimal monthlyPayment) {
            this.monthlyPayment = monthlyPayment;
            return this;
        }

        public LoanOfferDtoBuilder setIsInsuranceEnabled(Boolean isInsuranceEnabled) {
            this.isInsuranceEnabled = isInsuranceEnabled;
            return this;
        }

        public LoanOfferDtoBuilder setIsSalaryClient(Boolean isSalaryClient) {
            this.isSalaryClient = isSalaryClient;
            return this;
        }

        public LoanOfferDTO build() {
            return new LoanOfferDTO(this);
        }
    }
}
