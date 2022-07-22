package com.example.deal.entity;

import com.example.deal.dto.EmploymentDTO;
import com.example.deal.dto.Passport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "client")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String email;
    private Integer dependentAmount;

    @Embedded
    private Passport passport;

    @Embedded
    private EmploymentDTO employment;
    private String account;
}
