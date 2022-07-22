package com.example.deal.dto;

import com.example.deal.enums.Theme;
import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class EmailMessage {
    private String address;
    private Theme theme;
    private Long applicationId;
    private File documentAboutClient;
    private File documentAboutCredit;
    private File documentAboutPayment;
    private String code;
    private String Answer;
}
