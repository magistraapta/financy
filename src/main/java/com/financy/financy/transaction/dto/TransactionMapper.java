package com.financy.financy.transaction.dto;

import org.springframework.stereotype.Component;

import com.financy.financy.transaction.entity.Transaction;

@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setCategory(transaction.getCategory());
        dto.setDate(transaction.getDate());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        return dto;
    }

    public Transaction toEntity(TransactionDto dto) {
        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setCategory(dto.getCategory());
        transaction.setDate(dto.getDate());
        transaction.setCreatedAt(dto.getCreatedAt());
        transaction.setUpdatedAt(dto.getUpdatedAt());
        return transaction;
    }
}
