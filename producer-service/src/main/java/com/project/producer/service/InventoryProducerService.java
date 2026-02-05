package com.project.producer.service;

import com.project.producer.clientTest.InventoryClient;
import com.project.producer.model.BaseEvent;
import com.project.producer.publisher.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * InventoryProducerService is responsible for producing inventory events
 * fetched from the Inventory system and publishing them to Kafka.
 * <p>
 * This service uses {@link InventoryClient} to fetch product data and
 * {@link KafkaEventPublisher} to send events asynchronously to the
 * {@code inventory_data} Kafka topic.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * @RestController
 * public class InventoryController {
 *     private final InventoryProducerService inventoryProducerService;
 *
 *     public InventoryController(InventoryProducerService inventoryProducerService) {
 *         this.inventoryProducerService = inventoryProducerService;
 *     }
 *
 *     @PostMapping("/produce/inventory")
 *     public ResponseEntity<String> produce() {
 *         inventoryProducerService.produce();
 *         return ResponseEntity.ok("Inventory events published");
 *     }
 * }
 * }</pre>
 * </p>
 */
@Service
@RequiredArgsConstructor
public class InventoryProducerService {

    /**
     * Inventory client used to fetch product data from the Inventory system.
     */
    private final InventoryClient inventoryClient;

    /**
     * Kafka publisher used to send inventory events to Kafka topics.
     */
    private final KafkaEventPublisher publisher;

    /**
     * Fetches product data from the Inventory system and publishes each record
     * as a {@link BaseEvent} to the {@code inventory_data} Kafka topic.
     * <p>
     * Each event contains a unique event ID, source system metadata,
     * and the actual product payload.
     * </p>
     */
    public void produce() {
        List<Map<String, Object>> products = inventoryClient.fetchProducts();

        products.forEach(product -> {
            BaseEvent<Object> event = new BaseEvent<>("INVENTORY_UPDATE", "INVENTORY", product);
            publisher.publish("inventory_data", event.getEventId(), event);
        });
    }
}

