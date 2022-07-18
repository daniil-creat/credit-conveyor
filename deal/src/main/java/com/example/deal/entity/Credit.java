package com.example.deal.entity;

import com.example.deal.dto.PaymentScheduleElement;
import com.example.deal.enums.CreditStatus;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "credit")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

    @ElementCollection
    @CollectionTable(name="payment_schedul_element", joinColumns=@JoinColumn(name="credit_id"))
    private List<PaymentScheduleElement> paymentSchedule;
    private CreditStatus creditStatus;
}
