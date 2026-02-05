package com.project.producer.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * MockCustomerController provides a simple in-memory REST API to simulate
 * customer data management.
 * <p>
 * This controller exposes endpoints to fetch and add customers. It includes
 * sample data preloaded for testing and integration purposes.
 * </p>
 */
@RestController
@RequestMapping("/customers")
public class MockCustomerController {

    /**
     * In-memory storage for customer records. The key is a unique UUID,
     * and the value is a map representing customer attributes (e.g., name, email).
     */
    private final Map<String, Map<String, Object>> customerStore = new HashMap<>();

    /**
     * Constructor initializes the controller with some sample customer data.
     */
    public MockCustomerController() {
        addSampleCustomer("Alice Smith", "alice@example.com");
        addSampleCustomer("Bob Johnson", "bob@example.com");
        addSampleCustomer("Charlie Brown", "charlie@example.com");
    }

    /**
     * Helper method to add a sample customer to the store.
     *
     * @param name  customer name
     * @param email customer email
     */
    private void addSampleCustomer(String name, String email) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> customer = new HashMap<>();
        customer.put("id", id);
        customer.put("name", name);
        customer.put("email", email);
        customerStore.put(id, customer);
    }

    /**
     * GET /customers
     * <p>
     * Retrieves all customers from the mock store.
     * </p>
     *
     * @return a list of customers, where each customer is represented as a map
     */
    @GetMapping
    public List<Map<String, Object>> getCustomers() {
        return new ArrayList<>(customerStore.values());
    }

    /**
     * POST /customers
     * <p>
     * Adds a new customer to the mock store. A unique UUID is generated as the
     * customer ID, and it is added to the request payload.
     * </p>
     *
     * @param customer a map containing customer attributes (e.g., name, email)
     * @return the newly added customer, including the generated ID
     */
    @PostMapping
    public Map<String, Object> addCustomer(@RequestBody Map<String, Object> customer) {
        String id = UUID.randomUUID().toString();
        customer.put("id", id);
        customerStore.put(id, customer);
        return customer;
    }
}
