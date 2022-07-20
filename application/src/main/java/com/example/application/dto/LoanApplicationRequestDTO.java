package com.example.conveyor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanApplicationRequestDTO {
    @Min(value = 100000, message = "amount must be 100000 and more")
    private BigDecimal amount;

    @Min(value = 6, message = "term must be 6 and more months")
    private Integer term;

    @Size(min = 2, max = 30, message = "must be from 2 to 30 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "must be Latin letters")
    private String firstName;

    @Size(min = 2, max = 30, message = "must be from 2 to 30 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "must be Latin letters")
    private String lastName;

    @Pattern(regexp = "^.{0}$|^[A-Za-z]{2,30}+$",
            message = "it should be from 2 to 30 Latin characters")
    private String middleName;

    @Email(message = "must be [from 2 to 50 characters]@[from 2 to 30 characters]")
    private String email;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Pattern(regexp = "^[0-9]{4}+$", message = "must be 4 digits")
    private String passportSeries;

    @Pattern(regexp = "^[0-9]{6}+$", message = "must be 6 digits")
    private String passportNumber;
}
