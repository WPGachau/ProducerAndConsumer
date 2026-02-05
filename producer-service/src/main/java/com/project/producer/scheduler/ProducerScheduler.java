package com.project.producer.scheduler;

import com.project.producer.service.CustomerProducerService;
import com.project.producer.service.InventoryProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ProducerScheduler is responsible for periodically triggering the production
 * of customer and inventory events.
 * <p>
 * The scheduler interval is configurable via application properties:
 * <pre>{@code
 * producer.scheduler.fixed-delay-ms=60000
 * }</pre>
 * <p>
 * This allows changing the schedule interval without modifying the code.
 * </p>
 *
 * <p>
 * This class calls the {@link CustomerProducerService} and
 * {@link InventoryProducerService} to fetch data and publish events
 * to Kafka at fixed intervals.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ProducerScheduler {

    /**
     * Service responsible for producing customer events.
     */
    private final CustomerProducerService customerProducer;

    /**
     * Service responsible for producing inventory events.
     */
    private final InventoryProducerService inventoryProducer;

    /**
     * Fixed delay between consecutive runs of the scheduled task, in milliseconds.
     * Configurable via {@code producer.scheduler.fixed-delay-ms} in properties.
     */
    @Value("${producer.scheduler.fixed-delay-ms}")
    private long fixedDelay;

    /**
     * Periodically triggers production of customer and inventory events.
     * <p>
     * Uses Spring's {@link Scheduled} annotation with {@code fixedDelayString}
     * to read the interval from configuration properties.
     * </p>
     */
    @Scheduled(fixedDelayString = "${producer.scheduler.fixed-delay-ms}")
    public void run() {
        customerProducer.produce();
        inventoryProducer.produce();
    }
}
