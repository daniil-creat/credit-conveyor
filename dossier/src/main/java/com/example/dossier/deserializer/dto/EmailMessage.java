package com.example.dossier.deserializer.dto;

import com.example.dossier.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
