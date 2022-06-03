package com.example.conveyor.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreditDTO {

    private CreditDTO(CreditDtoBuilder builder){
        amount = builder.amount;
        term = builder.term;
        monthlyPayment = builder.monthlyPayment;
        rate = builder.rate;
        psk = builder.psk;
        isInsuranceEnabled = builder.isInsuranceEnabled;
        isSalaryClient = builder.isSalaryClient;
        paymentSchedule = builder.paymentSchedule;
    }

    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElement> paymentSchedule;

    public static class CreditDtoBuilder{
        private BigDecimal amount;
        private Integer term;
        private BigDecimal monthlyPayment;
        private BigDecimal rate;
        private BigDecimal psk;
        private Boolean isInsuranceEnabled;
        private Boolean isSalaryClient;
        private List<PaymentScheduleElement> paymentSchedule;

        public CreditDtoBuilder(){}

        public CreditDtoBuilder setAmount(BigDecimal amount){
            this.amount = amount;
            return this;
        }

        public CreditDtoBuilder setTerm(Integer term){
            this.term = term;
            return this;
        }

        public CreditDtoBuilder setMonthlyPayment(BigDecimal monthlyPayment){
            this.monthlyPayment = monthlyPayment;
            return this;
        }

        public CreditDtoBuilder setRate(BigDecimal rate){
            this.rate = rate;
            return this;
        }

        public CreditDtoBuilder setPsk(BigDecimal psk){
            this.psk = psk;
            return this;
        }

        public CreditDtoBuilder setIsInsuranceEnabled(Boolean isInsuranceEnabled){
            this.isInsuranceEnabled = isInsuranceEnabled;
            return this;
        }

        public CreditDtoBuilder setIsSalaryClient(Boolean isSalaryClient){
            this.isSalaryClient = isSalaryClient;
            return this;
        }

        public CreditDtoBuilder setPaymentSchedule(List<PaymentScheduleElement> paymentSchedule){
            this.paymentSchedule = paymentSchedule;
            return this;
        }

        public CreditDTO build() {
            return new CreditDTO(this);
        }
    }
}
