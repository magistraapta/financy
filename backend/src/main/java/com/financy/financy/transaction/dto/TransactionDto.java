package com.financy.financy.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.financy.financy.transaction.entity.TransactionType;
import com.financy.financy.transaction.entity.TransactionCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionCategory category;
    private LocalDateTime date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
