package com.project.producer.clientTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.project.producer.client;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class CrmClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private client.CrmClient crmClient;

    @Test
    public void testFetchCustomers_success() {
        // Mock API response
        List<Map<String, Object>> mockResponse = List.of(Map.of("name", "Alice"));
        when(restTemplate.getForObject(anyString(), eq(List.class))).thenReturn(mockResponse);

        List<Map<String, Object>> result = crmClient.fetchCustomers();
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).get("name"));
    }

    @Test
    public void testFetchCustomers_failureRetries() {
        // Simulate API failure
        when(restTemplate.getForObject(anyString(), eq(List.class)))
                .thenThrow(new RuntimeException("API down"));

        // Retryable annotation should retry 3 times, expect exception
        assertThrows(RuntimeException.class, () -> crmClient.fetchCustomers());
        verify(restTemplate, times(3)).getForObject(anyString(), eq(List.class));
    }
}

