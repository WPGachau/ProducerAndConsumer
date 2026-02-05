package com.project.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main entry point for the Producer Service Spring Boot application.
 * <p>
 * This class enables scheduling and asynchronous processing for producer services,
 * and serves as the bootstrap class for the Spring Boot application.
 * </p>
 *
 * <p>
 * Swagger/OpenAPI documentation is automatically enabled via SpringDoc dependency.
 * Access the documentation at:
 * <ul>
 *     <li>API Docs JSON: http://localhost:8080/api-docs</li>
 *     <li>Swagger UI: http://localhost:8080/swagger-ui.html</li>
 * </ul>
 * </p>
 */
@SpringBootApplication
@EnableScheduling  // Enables @Scheduled tasks
@EnableAsync       // Optional: enables async @Async methods
public class ProducerApplication {

    /**
     * Main method to launch the Spring Boot application.
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }
}

