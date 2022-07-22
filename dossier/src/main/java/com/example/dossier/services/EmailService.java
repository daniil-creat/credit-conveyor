package com.example.dossier.services;

import com.example.dossier.deserializer.dto.EmailMessage;

import javax.mail.MessagingException;

public interface EmailService {

    void sendEmail(EmailMessage content) throws MessagingException;

    void sendEmailForCreateDocuments(EmailMessage content) throws MessagingException;

    void sendEmailWithDocuments(EmailMessage content) throws MessagingException;

    void sendEmailWithCode(EmailMessage content) throws MessagingException;

    void sendEmailAboutAnswer(EmailMessage content) throws MessagingException;

    void sendEmailAboutAnswerDenied(EmailMessage content) throws MessagingException;
}
