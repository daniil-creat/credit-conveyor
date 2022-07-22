package com.example.dossier.consumer;

import com.example.dossier.deserializer.dto.EmailMessage;
import com.example.dossier.services.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@RequiredArgsConstructor
@Service
@Log4j2
public class Consumer {
    private final EmailServiceImpl emailService;

    @KafkaListener(topics = "finish-registration")
    public void consumeFinishRegistration(EmailMessage message) throws MessagingException {
        log.info("Start topic finish-registration");
        emailService.sendEmail(message);
    }

    @KafkaListener(topics = "create-documents")
    public void consumeCreateDocuments(EmailMessage message) throws MessagingException {
        log.info("Start topic create-documents");
        emailService.sendEmailForCreateDocuments(message);
    }

    @KafkaListener(topics = "send-documents")
    public void consumeSendDocuments(EmailMessage message) throws MessagingException {
        log.info("Start topic send-documents");
        emailService.sendEmailWithDocuments(message);
    }

    @KafkaListener(topics = "send-sign")
    public void consumeSendCode(EmailMessage message) throws MessagingException {
        log.info("Start topic send-sign");
        emailService.sendEmailWithCode(message);
    }

    @KafkaListener(topics = "credit-issued")
    public void consumeAnswer(EmailMessage message) throws MessagingException {
        log.info("Start topic credit-issued");
        emailService.sendEmailAboutAnswer(message);
    }

    @KafkaListener(topics = "credit-dinied")
    public void consumeAnswerDenied(EmailMessage message) throws MessagingException {
        log.info("Start topic credit-dinied");
        emailService.sendEmailAboutAnswer(message);
    }
}
