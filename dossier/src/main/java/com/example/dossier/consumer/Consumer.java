package com.example.dossier.consumer;

import com.example.dossier.deserializer.dto.EmailMessage;
import com.example.dossier.services.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@RequiredArgsConstructor
@Service
public class Consumer {
    private final EmailServiceImpl emailService;

    @KafkaListener(topics = "finish-registration")
    public void consumeFinishRegistration(EmailMessage message) throws MessagingException {
        emailService.sendEmail(message);
    }

    @KafkaListener(topics = "create-documents")
    public void consumeCreateDocuments(EmailMessage message) throws MessagingException {
        emailService.sendEmailForCreateDocuments(message);
    }

    @KafkaListener(topics = "send-documents")
    public void consumeSendDocuments(EmailMessage message) throws MessagingException {
        emailService.sendEmailWithDocuments(message);
    }

    @KafkaListener(topics = "send-sign")
    public void consumeSendCode(EmailMessage message) throws MessagingException {
        emailService.sendEmailWithCode(message);
    }

    @KafkaListener(topics = "credit-issued")
    public void consumeAnswer(EmailMessage message) throws MessagingException {
        emailService.sendEmailAboutAnswer(message);
    }

    @KafkaListener(topics = "credit-dinied")
    public void consumeAnswerDenied(EmailMessage message) throws MessagingException {
        emailService.sendEmailAboutAnswer(message);
    }
}
