package com.middleware.service.payment_gateway;

import com.middleware.service.payment_gateway.dtos.TransactionResponse;
import com.middleware.service.payment_gateway.service.BankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BankingServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private AsyncTaskExecutor taskExecutor;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private BankingService bankingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessTransaction() {
        // Arrange
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransRef("123");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        bankingService.processTransaction(transactionResponse);

        // Assert
        verify(taskExecutor, times(1)).execute(any(Runnable.class));
    }
}
