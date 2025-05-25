package com.financy.financy.transaction.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financy.financy.auth.CustomUserDetail;
import com.financy.financy.transaction.dto.TransactionDto;
import com.financy.financy.transaction.dto.TransactionMapper;
import com.financy.financy.transaction.entity.Transaction;
import com.financy.financy.transaction.entity.TransactionType;
import com.financy.financy.transaction.service.TransactionService;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.ok(transactionMapper.toDto(createdTransaction));
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userDetails.getUser().getId());
        return ResponseEntity.ok(transactions.stream().map(transactionMapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionMapper.toDto(transactionService.getTransactionById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found"))));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByUserId(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions.stream().map(transactionMapper::toDto).collect(Collectors.toList()));
    }

    // @GetMapping
    // public ResponseEntity<List<TransactionDto>> getTransactionsByUserIdAndTypeAndCreatedAtBetween(
    //         @RequestParam(required = false) TransactionType type,
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        
    //     if (startDate == null) {
    //         startDate = LocalDateTime.now().minusMonths(1); // Default to last month if not provided
    //     }
    //     if (endDate == null) {
    //         endDate = LocalDateTime.now(); // Default to current time if not provided
    //     }
        
    //     List<Transaction> transactions;
    //     if (type != null) {
    //         transactions = transactionService.getTransactions(userDetails.getUser().getId(), type, startDate, endDate);
    //     } else {
    //         transactions = transactionService.getTransactionsByUserId(userDetails.getUser().getId());
    //     }
        
    //     return ResponseEntity.ok(transactions.stream()
    //         .map(transactionMapper::toDto)
    //         .collect(Collectors.toList()));
    // }

    @GetMapping("/type")
    public ResponseEntity<List<TransactionDto>> getTransactionsByType(@RequestParam TransactionType type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        List<Transaction> transactions = transactionService.getTransactions(userDetails.getUser().getId(), type);
        return ResponseEntity.ok(transactions.stream().map(transactionMapper::toDto).collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        return transactionService.updateTransaction(id, transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }
}
