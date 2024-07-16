package com.middleware.service.payment_gateway;

import com.middleware.service.payment_gateway.enums.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RetailControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testInitiateTransaction() throws Exception {
        String requestBody = "{\"amount\": 100.0, \"description\": \"transportation fare\"}";
        mockMvc.perform(
                post("/api/v1/retail/transactions").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transRef").exists())
                .andExpect(jsonPath("$.status").value(TransactionStatus.PENDING.getValue()));
    }

    @Test
    void testGetTransaction() throws Exception {
        // create a transaction
        String requestBody = "{\"amount\": 100.0, \"description\": \"Payment for Clothes\"}";
        String transRef = mockMvc.perform(
                post("/api/v1/retail/transactions").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn().getResponse().getContentAsString().split("\"transRef\":\"")[1].split("\"")[0];

        // retrieve the transaction
        mockMvc.perform(
                get("/api/v1/retail/transactions/" + transRef))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transRef").value(transRef))
                .andExpect(jsonPath("$.status").exists());
    }
}
