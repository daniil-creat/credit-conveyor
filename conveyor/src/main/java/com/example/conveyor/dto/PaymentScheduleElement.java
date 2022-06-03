package com.example.conveyor.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentScheduleElement {

    private PaymentScheduleElement(PaymentScheduleElementBuilder builder) {
        number = builder.number;
        date = builder.date;
        totalPayment = builder.totalPayment;
        interestPayment = builder.interestPayment;
        debtPayment = builder.debtPayment;
        remainingDebt = builder.remainingDebt;
    }

    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;

    public static class PaymentScheduleElementBuilder {
        private Integer number;
        private LocalDate date;
        private BigDecimal totalPayment;
        private BigDecimal interestPayment;
        private BigDecimal debtPayment;
        private BigDecimal remainingDebt;

        public PaymentScheduleElementBuilder() {
        }

        public PaymentScheduleElementBuilder setNumber(Integer number) {
            this.number = number;
            return this;
        }

        public PaymentScheduleElementBuilder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public PaymentScheduleElementBuilder setTotalPayment(BigDecimal totalPayment) {
            this.totalPayment = totalPayment;
            return this;
        }

        public PaymentScheduleElementBuilder setInterestPayment(BigDecimal interestPayment) {
            this.interestPayment = interestPayment;
            return this;
        }

        public PaymentScheduleElementBuilder setDebtPayment(BigDecimal debtPayment) {
            this.debtPayment = debtPayment;
            return this;
        }

        public PaymentScheduleElementBuilder setRemainingDebt(BigDecimal remainingDebt) {
            this.remainingDebt = remainingDebt;
            return this;
        }

        public PaymentScheduleElement build() {
            return new PaymentScheduleElement(this);
        }
    }

}
