package com.financy.financy.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.financy.financy.transaction.entity.Transaction;
import com.financy.financy.transaction.entity.TransactionType;
import com.financy.financy.transaction.repository.TransactionRepository;
import com.financy.financy.auth.entity.User;
import com.financy.financy.auth.repository.AuthRepository;

@DataJpaTest
public class TransactionRepositoryTests {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AuthRepository authRepository;
    
    @Test
    void testSaveTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setType(TransactionType.INCOME);
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        assertNotNull(transaction.getId());
        assertEquals(BigDecimal.valueOf(100), transaction.getAmount());
        assertNotNull(transaction.getCreatedAt());
        assertNotNull(transaction.getUpdatedAt());
    }

    @Test
    void testFindByUserId() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user = authRepository.save(user); // Save user first to get a valid ID
        
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType(TransactionType.INCOME);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        
        // Act
        transactionRepository.save(transaction);
        List<Transaction> foundTransactions = transactionRepository.findByUserId(user.getId());
        
        // Assert
        assertNotNull(foundTransactions);
        assertFalse(foundTransactions.isEmpty());
        assertEquals(1, foundTransactions.size());
        assertEquals(user.getId(), foundTransactions.get(0).getUser().getId());
        assertEquals(transaction.getAmount(), foundTransactions.get(0).getAmount());
        assertEquals(transaction.getType(), foundTransactions.get(0).getType());
    }

    @Test
    void testFindByUserIdNotFound() {
        Long userId = 1L;
        List<Transaction> transaction = transactionRepository.findByUserId(userId);
        assertNotNull(transaction);
    }

    @Test
    void testFindByUserIdAndType() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user = authRepository.save(user);
        
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType(TransactionType.INCOME);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        
        transactionRepository.save(transaction);

        List<Transaction> foundTransactions = transactionRepository.findByUserIdAndType(user.getId(), TransactionType.INCOME);
        assertNotNull(foundTransactions);
        assertFalse(foundTransactions.isEmpty());
        assertEquals(1, foundTransactions.size());
        assertEquals(user.getId(), foundTransactions.get(0).getUser().getId());
        assertEquals(transaction.getAmount(), foundTransactions.get(0).getAmount());
        assertEquals(transaction.getType(), foundTransactions.get(0).getType());
    }
}
