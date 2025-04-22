package com.example.shop.infrastructure.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserActiveProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public UserActiveProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

//    public void sendMessage(KafkaMessageDTO messageDTO) {
//        try {
//            String message = objectMapper.writeValueAsString(messageDTO);
//            kafkaTemplate.send("first-topic", message);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Failed to serialize message", e);
//        }
//    }
}
