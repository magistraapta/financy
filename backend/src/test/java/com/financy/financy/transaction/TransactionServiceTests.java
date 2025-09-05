package com.financy.financy.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.financy.financy.auth.entity.User;
import com.financy.financy.auth.repository.AuthRepository;
import com.financy.financy.transaction.entity.Transaction;
import com.financy.financy.transaction.entity.TransactionType;
import com.financy.financy.transaction.repository.TransactionRepository;
import com.financy.financy.transaction.service.TransactionService;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock 
    private AuthRepository authRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User testUser;
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
    }
    
    @Test
    void testSaveTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setType(TransactionType.INCOME);
        transaction.setUser(testUser);
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setCreatedAt(LocalDateTime.now());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        
        Transaction savedTransaction = transactionService.createTransaction(transaction);
        assertEquals(transaction, savedTransaction);
    }

    @Test
    void testFindByUserId() {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setType(TransactionType.INCOME);
        transaction.setUser(testUser);
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setCreatedAt(LocalDateTime.now());

        when(transactionRepository.findByUserId(TEST_USER_ID)).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getTransactionsByUserId(TEST_USER_ID);
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    @Test
    void testFindByUserIdNotFound() {
        when(transactionRepository.findByUserId(TEST_USER_ID)).thenReturn(List.of());
        
        List<Transaction> transactions = transactionService.getTransactionsByUserId(TEST_USER_ID);
        assertEquals(0, transactions.size());
    }

    @Test
    void testFindByUserIdAndType() {
        TransactionType type = TransactionType.INCOME;
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setType(type);
        transaction.setUser(testUser);
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setCreatedAt(LocalDateTime.now());

        when(transactionRepository.findByUserIdAndType(TEST_USER_ID, type)).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getTransactions(TEST_USER_ID, type);
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    @Test
    void testFindByUserIdAndTypeNotFound() {
        when(transactionRepository.findByUserIdAndType(TEST_USER_ID, TransactionType.INCOME)).thenReturn(List.of());
        
        List<Transaction> transactions = transactionService.getTransactions(TEST_USER_ID, TransactionType.INCOME);
        assertEquals(0, transactions.size());
    }
}
