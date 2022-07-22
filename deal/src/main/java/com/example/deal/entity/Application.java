package com.example.deal.entity;

import com.example.deal.dto.ApplicationStatusHistoryDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "application")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private Client client;
    @OneToOne(fetch = FetchType.EAGER)
    private Credit credit;
    private Status status;
    private LocalDate creationDate;

    @Embedded
    private LoanOfferDTO appliedOffer;
    private LocalDate signDate;
    private String sesCode;

    @ElementCollection
    @CollectionTable(name="status_history", joinColumns=@JoinColumn(name="application_id"))
    private List<ApplicationStatusHistoryDTO> statusHistory;

}
