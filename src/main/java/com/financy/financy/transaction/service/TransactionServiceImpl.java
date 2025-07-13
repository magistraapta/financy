package com.financy.financy.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.financy.financy.transaction.entity.Transaction;
import com.financy.financy.transaction.entity.TransactionType;
import com.financy.financy.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        // Handle date if it's a string
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }
        
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Override
    public List<Transaction> getTransactions(Long userId, TransactionType type) {
        return transactionRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public List<Transaction> getTransactions(Long userId, TransactionType type, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserIdAndTypeAndCreatedAtBetween(userId, type, startDate, endDate);
    }

    @Override
    public Transaction getTransaction(Long id) {
        return transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
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

    @Override
    public BigDecimal getTotalBalance(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return transactions.stream()
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Double getTotalExpensesByUserId(Long userId) {
        return transactionRepository.getTotalExpensesByUserId(userId);
    }

    @Override
    public Double getTotalIncomeByUserId(Long userId) {
        return transactionRepository.getTotalIncomeByUserId(userId);
    }

    @Override
    public Double getCurrentMonthIncomeByUserId(Long userId) {
        return transactionRepository.getCurrentMonthIncomeByUserId(userId);
    }

    @Override
    public Double getPreviousMonthIncomeByUserId(Long userId) {
        return transactionRepository.getPreviousMonthIncomeByUserId(userId);
    }

    @Override
    public Double getCurrentMonthExpensesByUserId(Long userId) {
        return transactionRepository.getCurrentMonthExpensesByUserId(userId);
    }

    @Override
    public Double getPreviousMonthExpensesByUserId(Long userId) {
        return transactionRepository.getPreviousMonthExpensesByUserId(userId);
    }
}
