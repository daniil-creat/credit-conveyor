package com.example.deal.dto;

import com.example.deal.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Data
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusHistoryDTO {
    private Status status;
    private LocalDate localDateTime;
    private Status changeType;
}
