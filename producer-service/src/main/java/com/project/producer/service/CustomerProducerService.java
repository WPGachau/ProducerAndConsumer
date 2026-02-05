package com.project.producer.service;

import com.project.producer.clientTest.CrmClient;
import com.project.producer.model.BaseEvent;
import com.project.producer.publisher.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * CustomerProducerService is responsible for producing customer events
 * fetched from the CRM system and publishing them to Kafka.
 * <p>
 * This service leverages {@link CrmClient} to fetch customer data and
 * {@link KafkaEventPublisher} to send events asynchronously to the
 * {@code customer_data} Kafka topic.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * @RestController
 * public class CustomerController {
 *     private final CustomerProducerService customerProducerService;
 *
 *     public CustomerController(CustomerProducerService customerProducerService) {
 *         this.customerProducerService = customerProducerService;
 *     }
 *
 *     @PostMapping("/produce/customers")
 *     public ResponseEntity<String> produce() {
 *         customerProducerService.produce();
 *         return ResponseEntity.ok("Customer events published");
 *     }
 * }
 * }</pre>
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomerProducerService {

    /**
     * CRM client used to fetch customer data from the CRM system.
     */
    private final CrmClient crmClient;

    /**
     * Kafka publisher used to send customer events to Kafka topics.
     */
    private final KafkaEventPublisher publisher;

    /**
     * Fetches customer data from the CRM and publishes each record
     * as a {@link BaseEvent} to the {@code customer_data} Kafka topic.
     * <p>
     * Each event contains a unique event ID, source system metadata,
     * and the actual payload.
     * </p>
     */
    public void produce() {
        List<Map<String, Object>> customers = crmClient.fetchCustomers();

        customers.forEach(customer -> {
            BaseEvent<Object> event = new BaseEvent<>("CUSTOMER_UPDATE", "CRM", customer);
            publisher.publish("customer_data", event.getEventId(), event);
        });
    }
}

