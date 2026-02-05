package com.project.producer.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Generic event wrapper for publishing messages to Kafka or any event-driven system.
 * <p>
 * This class encapsulates event metadata such as a unique event ID, source system,
 * event type, timestamp, and the payload. It is designed to provide a consistent
 * structure for all events produced by the system.
 * </p>
 *
 * <p>
 * The {@code eventId} ensures uniqueness and can be used for idempotency.
 * The {@code timestamp} records the time the event was created.
 * </p>
 *
 * @param <T> the type of the payload contained in the event
 */
public class BaseEvent<T> {

    /**
     * Unique identifier for the event.
     * Generated automatically using UUID.
     */
    private String eventId = UUID.randomUUID().toString();

    /**
     * Type of the event (e.g., CUSTOMER_UPDATE, INVENTORY_UPDATE).
     */
    private String eventType;

    /**
     * Source system that produced the event (e.g., CRM, Inventory).
     */
    private String sourceSystem;

    /**
     * Timestamp indicating when the event was created.
     */
    private Instant timestamp = Instant.now();

    /**
     * The actual payload of the event, can be any object representing the data.
     */
    private T payload;

    /**
     * Constructs a new BaseEvent with the specified type, source system, and payload.
     *
     * @param eventType    the type of the event
     * @param sourceSystem the system that produced the event
     * @param payload      the payload object
     */
    public BaseEvent(String eventType, String sourceSystem, T payload) {
        this.eventType = eventType;
        this.sourceSystem = sourceSystem;
        this.payload = payload;
    }

    /** @return the unique event ID */
    public String getEventId() { return eventId; }

    /** @return the type of the event */
    public String getEventType() { return eventType; }

    /** @return the source system that produced the event */
    public String getSourceSystem() { return sourceSystem; }

    /** @return the timestamp when the event was created */
    public Instant getTimestamp() { return timestamp; }

    /** @return the payload of the event */
    public T getPayload() { return payload; }
}

