package com.example.deal.services.impl;

import com.example.deal.dto.EmailMessage;
import com.example.deal.entity.Application;
import com.example.deal.enums.Theme;
import com.example.deal.producer.Producer;
import com.example.deal.services.ApplicationService;
import com.example.deal.services.EmailSevice;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailSevice {

    private final Producer producer;
    private final ApplicationService applicationService;

    @Override
    public void sendMessageForFinishRegistration(Application application) {
        log.info("Service: Email,sendMessageForFinishRegistration method, parameters: {}", application);
        producer.sendMessage(EmailMessage.builder().address(application.getClient().getEmail())
                .theme(Theme.FINISH_REGISTRATION)
                .applicationId(application.getId())
                .build());
    }

    @Override
    public void sendMessageForFinishRegistrationById(Long id) {
        log.info("Service: Email,sendMessageForFinishRegistrationById method, parameters: {}", id);
        Application application = applicationService.findById(id);
        producer.sendMessageForCreateDocuments(EmailMessage.builder().address(application.getClient().getEmail())
                .theme(Theme.CREATE_DOCUMENT)
                .applicationId(id)
                .build());
    }

    @Override
    public void sendMessageForCreateDocuments(Long applicationId, File fileClient, File fileCredit, File filePayment) {
        log.info("Service: Email,sendMessageForCreateDocuments method, parameters: {}, {},{},{}", applicationId, fileClient, fileCredit, filePayment);
        Application application = applicationService.findById(applicationId);
        producer.sendMessageForSendDocument(EmailMessage.builder().address(application.getClient().getEmail())
                .theme(Theme.DOCUMENTS)
                .applicationId(application.getId())
                .documentAboutClient(fileClient)
                .documentAboutCredit(fileCredit)
                .documentAboutPayment(filePayment)
                .build());
    }

    @Override
    public void sendMessageWithCode(Long id, String code) {
        log.info("Service: Email,sendMessageWithCode method, parameters: {}, {}", id, code);
        Application application = applicationService.findById(id);
        producer.sendMessageForSign(EmailMessage.builder().address(application.getClient().getEmail())
                .theme(Theme.SIGN)
                .applicationId(application.getId())
                .code(code)
                .build());
    }

    @Override
    public void sendMessageAboutAnswer(Boolean answer, Long id) {
        log.info("Service: Email,sendMessageAboutAnswer method, parameters: {}, {}", answer, id);
        if (answer == true) {
            Application application = applicationService.findById(id);
            producer.sendMessageAnswer(EmailMessage.builder().address(application.getClient().getEmail())
                    .theme(Theme.ANSWER)
                    .applicationId(application.getId())
                    .Answer("Credit agreed")
                    .build());
        }
    }

    @Override
    public void sendMessageAboutDenied(Long id) {
        log.info("Service: Email,sendMessageAboutDenied method, parameters: {}", id);
        Application application = applicationService.findById(id);
        producer.sendMessageAboutDenied(EmailMessage.builder().address(application.getClient().getEmail())
                .theme(Theme.ANSWER)
                .applicationId(application.getId())
                .Answer("Credit denied")
                .build());
    }
}
