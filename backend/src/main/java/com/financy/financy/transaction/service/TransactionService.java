package com.financy.financy.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.financy.financy.transaction.entity.Transaction;
import com.financy.financy.transaction.entity.TransactionType;

@Service
public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
    Optional<Transaction> getTransactionById(Long id);
    List<Transaction> getTransactionsByUserId(Long userId);
    List<Transaction> getTransactions(Long userId, TransactionType type);
    List<Transaction> getTransactions(Long userId, TransactionType type, LocalDateTime startDate, LocalDateTime endDate);
    Transaction getTransaction(Long id);
    void deleteTransaction(Long id);
    Transaction updateTransaction(Long id, Transaction transactionDetails);
    BigDecimal getTotalBalance(Long userId);
    Double getTotalExpensesByUserId(Long userId);
    Double getTotalIncomeByUserId(Long userId);
    Double getCurrentMonthIncomeByUserId(Long userId);
    Double getPreviousMonthIncomeByUserId(Long userId);
    Double getCurrentMonthExpensesByUserId(Long userId);
    Double getPreviousMonthExpensesByUserId(Long userId);
}
