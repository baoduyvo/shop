package com.example.shop.infrastructure.common.util;

import com.example.shop.infrastructure.common.contants.EventType;
import com.example.shop.infrastructure.common.dto.KafkaMessage;
import com.example.shop.infrastructure.common.dto.MessageMeta;

import java.util.UUID;

public class MessageBuilder {
    public MessageBuilder() {
    }

    public static <T> KafkaMessage<T> build(String serviceId, EventType eventType, String messageCode, T payload) {
        KafkaMessage<T> message = new KafkaMessage();
        MessageMeta meta = MessageMeta.builder().messageId(generateMessageId()).serviceId(serviceId).type(eventType).timestamp(System.currentTimeMillis()).build();
        message.setMeta(meta);
        message.setMessageCode(messageCode);
        message.setPayload(payload);
        return message;
    }

    public static String generateMessageId() {
        return UUID.randomUUID().toString().replace("_", "");
    }
}