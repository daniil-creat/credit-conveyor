package com.example.dossier.services.impl;

import com.example.dossier.deserializer.dto.EmailMessage;
import com.example.dossier.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void sendEmail(EmailMessage content) throws MailException, MessagingException {
        String html = "<p>Hello! You loan application №" + content.getApplicationId() + " approved.</p>" +
                "<p>to continue registration, follow the link:</p>" +
                "<a href=\"http://localhost:8082/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT\">" +
                "http://localhost:8082/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT</a>";
        MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("credit-conveyor@mail.ru");
            messageHelper.setTo(content.getAddress());
            messageHelper.setSubject(content.getTheme().toString());
            messageHelper.setText(html, true);
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailForCreateDocuments(EmailMessage content) throws MessagingException {
        String html = "<p>Hello! you have passed all the checks by application №" + content.getApplicationId() + ".</p>" +
                "<p>to continue, follow the link:</p>" +
                "<a href=\"http://localhost:8082/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT\">" +
                "http://localhost:8082/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT</a>";
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("credit-conveyor@mail.ru");
        messageHelper.setTo(content.getAddress());
        messageHelper.setSubject(content.getTheme().toString());
        messageHelper.setText(html, true);
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailWithDocuments(EmailMessage content) throws MessagingException {
        String html = "<p>Hello! you have passed all the checks by application №" + content.getApplicationId() + ".</p>" +
                "<p>to continue, follow the link:</p>" +
                "<a href=\"http://localhost:8082/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT\">" +
                "http://localhost:8082/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT</a>";
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("credit-conveyor@mail.ru");
        messageHelper.setTo(content.getAddress());
        messageHelper.setSubject(content.getTheme().toString());
        messageHelper.setText(html, true);
        messageHelper.addAttachment("client-info.document.txt", content.getDocumentAboutClient());
        messageHelper.addAttachment("credit-info.document.txt", content.getDocumentAboutCredit());
        messageHelper.addAttachment("payment-info.document.txt", content.getDocumentAboutPayment());
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailWithCode(EmailMessage content) throws MessagingException {
        String html = "<p>your code for signing documents" + content.getCode() + ".</p>" +
                "<p>to continue, follow the link:</p>" +
                "<a href=\"http://localhost:8082/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT\">" +
                "http://localhost:8082/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT</a>";
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("credit-conveyor@mail.ru");
        messageHelper.setTo(content.getAddress());
        messageHelper.setSubject(content.getTheme().toString());
        messageHelper.setText(html, true);
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailAboutAnswer(EmailMessage content) throws MessagingException {
        String html = "<p>Credit agreed</p>";
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("credit-conveyor@mail.ru");
        messageHelper.setTo(content.getAddress());
        messageHelper.setSubject(content.getTheme().toString());
        messageHelper.setText(html, true);
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailAboutAnswerDenied(EmailMessage content) throws MessagingException {
        String html = "<p>Credit dinied</p>";
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("credit-conveyor@mail.ru");
        messageHelper.setTo(content.getAddress());
        messageHelper.setSubject(content.getTheme().toString());
        messageHelper.setText(html, true);
        emailSender.send(mimeMessage);
    }
}


