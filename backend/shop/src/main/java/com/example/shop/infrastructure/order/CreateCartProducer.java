package com.example.shop.infrastructure.order;

import com.example.shop.infrastructure.common.contants.EventType;
import com.example.shop.infrastructure.common.event.CartDetailEvent;
import com.example.shop.infrastructure.common.util.MessageBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CreateCartProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CreateCartProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(CartDetailEvent evt) {
        try {
            kafkaTemplate.send("shop-cart-public-created_prod-dev", evt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
}
