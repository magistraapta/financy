package com.financy.financy.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.financy.financy.transaction.entity.Transaction;
import com.financy.financy.transaction.entity.TransactionType;
import com.financy.financy.transaction.repository.TransactionRepository;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        // Handle date if it's a string
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }
        
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getTransactions(Long userId, TransactionType type) {
        return transactionRepository.findByUserIdAndType(userId, type);
    }

    public List<Transaction> getTransactions(Long userId, TransactionType type, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserIdAndTypeAndCreatedAtBetween(userId, type, startDate, endDate);
    }

    public Transaction getTransaction(Long id) {
        return transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id)
            .map(existingTransaction -> {
                existingTransaction.setAmount(transactionDetails.getAmount());
                existingTransaction.setType(transactionDetails.getType());
                existingTransaction.setCategory(transactionDetails.getCategory());
                existingTransaction.setDate(transactionDetails.getDate());
                existingTransaction.setUpdatedAt(LocalDateTime.now());
                return transactionRepository.save(existingTransaction);
            })
            .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    public BigDecimal getTotalBalance(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return transactions.stream()
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Double getTotalExpensesByUserId(Long userId) {
        return transactionRepository.getTotalExpensesByUserId(userId);
    }

    public Double getTotalIncomeByUserId(Long userId) {
        return transactionRepository.getTotalIncomeByUserId(userId);
    }
}
