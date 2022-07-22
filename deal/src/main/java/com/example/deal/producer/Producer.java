package com.example.deal.producer;

import com.example.deal.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class Producer {
    private static final String TOPIC_ONE = "finish-registration";
    private static final String TOPIC_TWO = "create-documents";
    private static final String TOPIC_THREE = "send-documents";
    private static final String TOPIC_FOUR = "send-sign";
    private static final String TOPIC_FIVE = "credit-issued";
    private static final String TOPIC_SIX = "credit-denied";

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void sendMessage(EmailMessage message){
        this.kafkaTemplate.send(TOPIC_ONE, message);
    }

    public void sendMessageForCreateDocuments(EmailMessage message){
        this.kafkaTemplate.send(TOPIC_TWO, message);
    }

    public void sendMessageForSendDocument(EmailMessage message){
        this.kafkaTemplate.send(TOPIC_THREE, message);
    }

    public void sendMessageForSign(EmailMessage message){
        this.kafkaTemplate.send(TOPIC_FOUR, message);
    }

    public void sendMessageAnswer(EmailMessage message){
        this.kafkaTemplate.send(TOPIC_FIVE, message);
    }
    public void sendMessageAboutDenied(EmailMessage message){
        this.kafkaTemplate.send(TOPIC_SIX, message);
    }
}
