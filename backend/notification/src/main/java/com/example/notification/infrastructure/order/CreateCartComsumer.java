package com.example.notification.infrastructure.order;


import com.example.notification.dtos.mail.KafkaMessageDTO;
import com.example.notification.infrastructure.common.CartDetailEvent;
import com.example.notification.services.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
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

    @SneakyThrows
    @RetryableTopic(
            attempts = "3", // Allow up to 3 retry attempts
            backoff = @Backoff(delay = 1000, multiplier = 2.0), // 1 second delay, doubling after each attempt
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            autoCreateTopics = "false",
            include = {RetriableException.class}
    )
    @KafkaListener(
            groupId = "G00",
            topics = "shop-cart-public-created_prod-dev",
            properties = {"spring.json.value.default.type=com.example.notification.infrastructure.common.CartDetailEvent"},
            concurrency = "3"
    )
    public void listen(CartDetailEvent message) {
        logger.info("Notification");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(message);
        logger.info("Json message received using Kafka listener " + jsonString);
        mailService.send(new KafkaMessageDTO("voduybao053@gmail.com", "voduybao053@gmail.com", "consumer", jsonString));
    }
}
