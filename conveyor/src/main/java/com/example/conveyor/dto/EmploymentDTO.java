package com.example.conveyor.dto;

import com.example.conveyor.enums.EmploymentStatus;
import com.example.conveyor.enums.Position;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
