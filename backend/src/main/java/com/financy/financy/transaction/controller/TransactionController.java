package com.financy.financy.transaction.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import com.financy.financy.transaction.dto.ExpenseComparisonDto;
import com.financy.financy.transaction.dto.IncomeComparisonDto;
import com.financy.financy.transaction.dto.TotalAmountDto;
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

    @GetMapping("/total-income")
    public ResponseEntity<Double> getTotalIncome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Double totalIncome = transactionService.getTotalIncomeByUserId(userDetails.getUser().getId());

        TotalAmountDto totalAmountDto = transactionMapper.toTotalAmountDto(totalIncome);
        return ResponseEntity.ok(totalAmountDto.getAmount());
    }

    @GetMapping("/total-expenses")
    public ResponseEntity<Double> getTotalExpenses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Double totalExpenses = transactionService.getTotalExpensesByUserId(userDetails.getUser().getId());

        TotalAmountDto totalAmountDto = transactionMapper.toTotalAmountDto(totalExpenses);
        return ResponseEntity.ok(totalAmountDto.getAmount());
    }

    @GetMapping("/current-month-income")
    public ResponseEntity<Double> getCurrentMonthIncome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Double currentMonthIncome = transactionService.getCurrentMonthIncomeByUserId(userDetails.getUser().getId());
        return ResponseEntity.ok(currentMonthIncome);
    }

    @GetMapping("/previous-month-income")
    public ResponseEntity<Double> getPreviousMonthIncome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Double previousMonthIncome = transactionService.getPreviousMonthIncomeByUserId(userDetails.getUser().getId());
        return ResponseEntity.ok(previousMonthIncome);
    }

    @GetMapping("/income-comparison")
    public ResponseEntity<IncomeComparisonDto> getIncomeComparison() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        
        Double currentMonthIncome = transactionService.getCurrentMonthIncomeByUserId(userDetails.getUser().getId());
        Double previousMonthIncome = transactionService.getPreviousMonthIncomeByUserId(userDetails.getUser().getId());
        
        Double difference = currentMonthIncome - previousMonthIncome;
        Double percentageChange = previousMonthIncome > 0 ? (difference / previousMonthIncome) * 100 : 0.0;
        
        String trend;
        if (difference > 0) {
            trend = "increase";
        } else if (difference < 0) {
            trend = "decrease";
        } else {
            trend = "no-change";
        }
        
        IncomeComparisonDto comparison = new IncomeComparisonDto(
            currentMonthIncome, 
            previousMonthIncome, 
            difference, 
            percentageChange, 
            trend
        );
        
        return ResponseEntity.ok(comparison);
    }

    @GetMapping("/expense-this-month")
    public ResponseEntity<Double> getExpenseThisMonth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Double expenseThisMonth = transactionService.getCurrentMonthExpensesByUserId(userDetails.getUser().getId());
        return ResponseEntity.ok(expenseThisMonth);
    }

    @GetMapping("/expense-previous-month")
    public ResponseEntity<Double> getExpensePreviousMonth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Double expensePreviousMonth = transactionService.getPreviousMonthExpensesByUserId(userDetails.getUser().getId());
        return ResponseEntity.ok(expensePreviousMonth);
    }

    @GetMapping("/expense-comparison")
    public ResponseEntity<ExpenseComparisonDto> getExpenseComparison() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        
        Double currentMonthExpenses = transactionService.getCurrentMonthExpensesByUserId(userDetails.getUser().getId());
        Double previousMonthExpenses = transactionService.getPreviousMonthExpensesByUserId(userDetails.getUser().getId());
        
        Double difference = currentMonthExpenses - previousMonthExpenses;
        Double percentageChange = previousMonthExpenses > 0 ? (difference / previousMonthExpenses) * 100 : 0.0;
        
        String trend;
        if (difference > 0) {
            trend = "increase";
        } else if (difference < 0) {
            trend = "decrease";
        } else {
            trend = "no-change";
        }

        ExpenseComparisonDto comparison = new ExpenseComparisonDto(
            currentMonthExpenses, 
            previousMonthExpenses, 
            difference,
            percentageChange,
            trend
        );

        return ResponseEntity.ok(comparison);
    }
}
