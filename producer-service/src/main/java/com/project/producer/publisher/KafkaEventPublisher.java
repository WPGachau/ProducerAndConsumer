package com.project.producer.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * KafkaEventPublisher is responsible for publishing events to Kafka topics.
 * <p>
 * This component leverages Spring Kafka's {@link KafkaTemplate} to asynchronously
 * send messages to a specified topic with a key and payload. It includes logging
 * for both success and failure scenarios.
 * </p>
 *
 * <p>
 * Integration with SpringDoc OpenAPI is automatic for endpoints that use this
 * publisher in controllers or services. Ensure the following dependency is added
 * to your pom.xml for Swagger/OpenAPI UI:
 * </p>
 *
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;org.springdoc&lt;/groupId&gt;
 *     &lt;artifactId&gt;springdoc-openapi-starter-webmvc-ui&lt;/artifactId&gt;
 *     &lt;version&gt;2.2.0&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * <p>
 * Usage example:
 * <pre>{@code
 * @RestController
 * public class MyController {
 *     private final KafkaEventPublisher publisher;
 *
 *     public MyController(KafkaEventPublisher publisher) {
 *         this.publisher = publisher;
 *     }
 *
 *     @PostMapping("/send")
 *     public ResponseEntity<String> sendEvent(@RequestBody MyPayload payload) {
 *         publisher.publish("my_topic", payload.getId(), payload);
 *         return ResponseEntity.ok("Event published");
 *     }
 * }
 * }</pre>
 * </p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher {

    /**
     * KafkaTemplate used for publishing messages.
     */
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publishes a message to the specified Kafka topic asynchronously.
     *
     * @param topic   The Kafka topic to publish to
     * @param key     The key for the message (used for partitioning)
     * @param payload The payload object to send
     */
    public void publish(String topic, String key, Object payload) {
        kafkaTemplate.send(topic, key, payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish to {}", topic, ex);
                    } else {
                        log.info("Published to {} offset={}", topic, result.getRecordMetadata().offset());
                    }
                });
    }
}
