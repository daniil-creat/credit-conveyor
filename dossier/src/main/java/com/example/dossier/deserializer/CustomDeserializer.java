package com.example.dossier.deserializer;

import com.example.dossier.deserializer.dto.EmailMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomDeserializer implements Deserializer<EmailMessage> {
    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {

    }

    @Override
    public EmailMessage deserialize(String arg0, byte[] arg1) {
        ObjectMapper mapper = new ObjectMapper();
        EmailMessage user = null;
        try {
            user = mapper.readValue(arg1, EmailMessage.class);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return user;
    }
}
