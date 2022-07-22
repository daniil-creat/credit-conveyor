package com.example.dossier.services.impl;

import com.example.dossier.deserializer.dto.EmailMessage;
import com.example.dossier.services.EmailService;
import com.example.dossier.utils.MessageTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Service
@Log4j2
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void sendEmail(EmailMessage content) throws MailException, MessagingException {
        log.info("Start create message, parapetr content: {}", content);
        String body = MessageTemplate.getTemplateForFinishedRegistration(content.getApplicationId());
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("credit-conveyor@mail.ru");
        messageHelper.setTo(content.getAddress());
        messageHelper.setSubject(content.getTheme().toString());
        messageHelper.setText(body, true);
        log.info("Start send message");
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailForCreateDocuments(EmailMessage content) throws MessagingException {
        log.info("Start create message for create documents, parapetr content: {}", content);
        String body = MessageTemplate.getTemplateForCreateDocuments(content.getApplicationId());
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("credit-conveyor@mail.ru");
        messageHelper.setTo(content.getAddress());
        messageHelper.setSubject(content.getTheme().toString());
        messageHelper.setText(body, true);
        log.info("Start send message");
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailWithDocuments(EmailMessage content) throws MessagingException {
        log.info("Start create message with documents, parapetr content: {}", content);
        String body = MessageTemplate.getTamplateForEmailWithDocuments(content.getApplicationId());
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("credit-conveyor@mail.ru");
        messageHelper.setTo(content.getAddress());
        messageHelper.setSubject(content.getTheme().toString());
        messageHelper.setText(body, true);
        messageHelper.addAttachment("client-info.document.txt", content.getDocumentAboutClient());
        messageHelper.addAttachment("credit-info.document.txt", content.getDocumentAboutCredit());
        messageHelper.addAttachment("payment-info.document.txt", content.getDocumentAboutPayment());
        log.info("Start send message");
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailWithCode(EmailMessage content) throws MessagingException {
        log.info("Start create message with code, parapetr content: {}", content);
        String html = MessageTemplate.getTemplateForEmailWithCode(content.getCode());
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("credit-conveyor@mail.ru");
        messageHelper.setTo(content.getAddress());
        messageHelper.setSubject(content.getTheme().toString());
        messageHelper.setText(html, true);
        log.info("Start send message");
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailAboutAnswer(EmailMessage content) throws MessagingException {
        log.info("Start create message about answer, parapetr content: {}", content);
        String body = MessageTemplate.CREDIT_AGREED;
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("credit-conveyor@mail.ru");
        messageHelper.setTo(content.getAddress());
        messageHelper.setSubject(content.getTheme().toString());
        messageHelper.setText(body, true);
        log.info("Start send message");
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailAboutAnswerDenied(EmailMessage content) throws MessagingException {
        log.info("Start create message about answer denied, parapetr content: {}", content);
        String body = MessageTemplate.CREDIT_DENIED;
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("credit-conveyor@mail.ru");
        messageHelper.setTo(content.getAddress());
        messageHelper.setSubject(content.getTheme().toString());
        messageHelper.setText(body, true);
        log.info("Start send message");
        emailSender.send(mimeMessage);
    }
}


