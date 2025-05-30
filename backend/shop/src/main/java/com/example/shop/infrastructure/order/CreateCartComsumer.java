package com.example.shop.infrastructure.order;


import com.example.shop.dtos.reponse.mail.KafkaMessageDTO;
import com.example.shop.infrastructure.common.event.CartDetailEvent;
import com.example.shop.services.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateCartComsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(CreateCartComsumer.class);

    @Autowired
    private MailService mailService;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0 , maxDelay = 15000),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            autoCreateTopics = "false",
            include = {RetriableException.class}
    )
    @KafkaListener(
            groupId = "G00",
            topics = "shop-cart-public-created_prod-dev",
            concurrency = "3"
    )
    @SneakyThrows
    public void listen(@Payload CartDetailEvent message) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(message);
        logger.info("Json message received using Kafka listener " + jsonString);
        mailService.send(new KafkaMessageDTO("voduybao053@gmail.com", "voduybao053@gmail.com", "consumer", jsonString));
    }

    @DltHandler
    public void processMessage(CartDetailEvent message) {
        log.error("DltHandler processMessage = {}", message);
    }
}
