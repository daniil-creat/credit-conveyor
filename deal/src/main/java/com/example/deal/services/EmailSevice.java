package com.example.deal.services;

import com.example.deal.entity.Application;

import java.io.File;

public interface EmailSevice {
    void sendMessageForFinishRegistration(Application application);
    void sendMessageForFinishRegistrationById(Long id);
    void sendMessageForCreateDocuments(Long applicationId, File fileClient,File fileCredit,File filePayment);
    void sendMessageWithCode(Long applicationId, String code);
    void sendMessageAboutAnswer(Boolean answer, Long id);
    void sendMessageAboutDenied(Long id);
}
