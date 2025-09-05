package com.financy.financy.transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.financy.financy.auth.CustomUserDetailsService;
import com.financy.financy.auth.config.SecurityConfig;
import com.financy.financy.auth.entity.User;
import com.financy.financy.transaction.controller.TransactionController;
import com.financy.financy.transaction.dto.TransactionDto;
import com.financy.financy.transaction.dto.TransactionMapper;
import com.financy.financy.transaction.entity.Transaction;
import com.financy.financy.transaction.entity.TransactionType;
import com.financy.financy.transaction.service.TransactionService;

@WebMvcTest(TransactionController.class)
@Import(SecurityConfig.class)
public class TransactionControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionMapper transactionMapper;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;
    private Transaction testTransaction;
    private TransactionDto testTransactionDto;
    private static final Long TEST_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setId(TEST_USER_ID);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());

        // Create test transaction
        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setUser(testUser);
        testTransaction.setAmount(BigDecimal.valueOf(100));
        testTransaction.setType(TransactionType.INCOME);
        testTransaction.setCreatedAt(LocalDateTime.now());
        testTransaction.setUpdatedAt(LocalDateTime.now());

        // Create test transaction DTO
        testTransactionDto = new TransactionDto();
        testTransactionDto.setId(testTransaction.getId());
        testTransactionDto.setAmount(testTransaction.getAmount());
        testTransactionDto.setType(testTransaction.getType());
        testTransactionDto.setCreatedAt(testTransaction.getCreatedAt());
        testTransactionDto.setUpdatedAt(testTransaction.getUpdatedAt());

        // Mock service and mapper responses
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(testTransaction);
        when(transactionService.getTransactionById(1L)).thenReturn(Optional.of(testTransaction));
        when(transactionMapper.toDto(any(Transaction.class))).thenReturn(testTransactionDto);
    }

    @Test
    @WithMockUser(username = "testuser", password = "password")
    void testSaveTransaction() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 100, \"type\": \"INCOME\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", password = "password")
    void testFindByUserId() throws Exception {
        mockMvc.perform(get("/transactions/1"))
                .andExpect(status().isOk());
    }
}
