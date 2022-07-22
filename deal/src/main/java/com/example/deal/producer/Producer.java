package com.example.deal.producer;

import com.example.deal.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Log4j2
public class Producer {
    private static final String TOPIC_ONE = "finish-registration";
    private static final String TOPIC_TWO = "create-documents";
    private static final String TOPIC_THREE = "send-documents";
    private static final String TOPIC_FOUR = "send-sign";
    private static final String TOPIC_FIVE = "credit-issued";
    private static final String TOPIC_SIX = "credit-denied";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(EmailMessage message) {
        log.info("Start send message topic finish-registration");
        kafkaTemplate.send(TOPIC_ONE, message);
    }

    public void sendMessageForCreateDocuments(EmailMessage message) {
        log.info("Start send message topic create-documents");
        kafkaTemplate.send(TOPIC_TWO, message);
    }

    public void sendMessageForSendDocument(EmailMessage message) {
        log.info("Start send message topic finish-registration");
        kafkaTemplate.send(TOPIC_THREE, message);
    }

    public void sendMessageForSign(EmailMessage message) {
        log.info("Start send message topic finish-send-documents");
        kafkaTemplate.send(TOPIC_FOUR, message);
    }

    public void sendMessageAnswer(EmailMessage message) {
        log.info("Start send message topic credit-issued");
        kafkaTemplate.send(TOPIC_FIVE, message);
    }

    public void sendMessageAboutDenied(EmailMessage message) {
        log.info("Start send message topic credit-denied");
        kafkaTemplate.send(TOPIC_SIX, message);
    }
}
