package com.cognicart.identity_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "user-registered-topic";

    public void publishUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            log.info("Publishing UserRegisteredEvent to Kafka: {}", event);
            
            Message<UserRegisteredEvent> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, TOPIC)
                    .build();
            
            kafkaTemplate.send(message).whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Successfully published UserRegisteredEvent for userId: {}", event.getUserId());
                } else {
                    log.error("Failed to publish UserRegisteredEvent: ", ex);
                }
            });
        } catch (Exception e) {
            log.error("Error publishing UserRegisteredEvent: ", e);
        }
    }
}

