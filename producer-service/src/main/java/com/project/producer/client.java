package com.project.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class client {
    /**
     * CrmClient is responsible for fetching customer data from the CRM system.
     * <p>
     * This component uses Spring's {@link RestTemplate} to make REST API calls to
     * the CRM endpoint. It includes retry logic with exponential backoff to handle
     * temporary failures or network issues.
     * </p>
     *
     * <p>
     * The CRM base URL can be configured via the {@code crm.base-url} property in
     * {@code application.yml}. Defaults to {@code http://localhost:8081} if not set.
     * </p>
     *
     * <p>
     * Example usage:
     * <pre>{@code
     * @Service
     * public class CustomerService {
     *     private final CrmClient crmClient;
     *
     *     public CustomerService(CrmClient crmClient) {
     *         this.crmClient = crmClient;
     *     }
     *
     *     public void processCustomers() {
     *         List<Map<String, Object>> customers = crmClient.fetchCustomers();
     *         // process customer data
     *     }
     * }
     * }</pre>
     * </p>
     */
    @Component
    @RequiredArgsConstructor
    @Slf4j
    public static class CrmClient {

        /**
         * RestTemplate used for making HTTP requests to the CRM API.
         */
        private final RestTemplate restTemplate;

        /**
         * Base URL for the CRM system.
         * Configurable via {@code crm.base-url} in application properties.
         */
        @Value("${crm.base-url:http://localhost:8081}")
        private String crmUrl;

        /**
         * Fetches the list of customers from the CRM REST API.
         * <p>
         * This method is retried up to 3 times in case of failure, with an exponential
         * backoff starting at 2 seconds.
         * </p>
         *
         * @return a {@link List} of {@link Map} objects representing customers
         */
        @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
        public List<Map<String, Object>> fetchCustomers() {
            log.info("Fetching customers from CRM at {}", crmUrl);
            return restTemplate.getForObject(crmUrl + "/customers", List.class);
        }
    }

    /**
     * InventoryClient is responsible for fetching product data from the Inventory system.
     * <p>
     * This component uses Spring's {@link RestTemplate} to call the Inventory REST API.
     * It includes retry logic with exponential backoff to handle temporary network or service failures.
     * </p>
     *
     * <p>
     * The Inventory base URL can be configured via the {@code inventory.base-url} property in
     * {@code application.yml}. Defaults to {@code http://localhost:8082} if not set.
     * </p>
     *
     * <p>
     * Example usage:
     * <pre>{@code
     * @Service
     * public class ProductService {
     *     private final InventoryClient inventoryClient;
     *
     *     public ProductService(InventoryClient inventoryClient) {
     *         this.inventoryClient = inventoryClient;
     *     }
     *
     *     public void processProducts() {
     *         List<Map<String, Object>> products = inventoryClient.fetchProducts();
     *         // process product data
     *     }
     * }
     * }</pre>
     * </p>
     */
    @Component
    @RequiredArgsConstructor
    @Slf4j
    public static class InventoryClient {

        /**
         * RestTemplate used for making HTTP requests to the Inventory API.
         */
        private final RestTemplate restTemplate;

        /**
         * Base URL for the Inventory system.
         * Configurable via {@code inventory.base-url} in application properties.
         */
        @Value("${inventory.base-url:http://localhost:8082}")
        private String inventoryUrl;

        /**
         * Fetches the list of products from the Inventory REST API.
         * <p>
         * This method is retried up to 3 times in case of failure, with an exponential
         * backoff starting at 2 seconds between retries.
         * </p>
         *
         * @return a {@link List} of {@link Map} objects representing products
         */
        @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
        public List<Map<String, Object>> fetchProducts() {
            log.info("Fetching products from Inventory at {}", inventoryUrl);
            return restTemplate.getForObject(inventoryUrl + "/products", List.class);
        }
    }
}
