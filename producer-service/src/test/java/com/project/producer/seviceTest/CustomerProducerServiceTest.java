package com.project.producer.seviceTest;

import static org.mockito.Mockito.*;

import com.project.producer.client;
import com.project.producer.publisher.KafkaEventPublisher;
import com.project.producer.service.CustomerProducerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;

public class CustomerProducerServiceTest {

    @Mock
    private client.CrmClient crmClient;

    @Mock
    private KafkaEventPublisher publisher;

    @InjectMocks
    private CustomerProducerService customerProducerService;

    @Test
    public void testProduce_callsKafkaPublisher() {
        when(crmClient.fetchCustomers()).thenReturn(
                List.of(Map.of("name", "Alice"))
        );

        customerProducerService.produce();

        // Verify Kafka publisher is called
        verify(publisher, times(1)).publish(eq("customer_data"), anyString(), any());
    }
}