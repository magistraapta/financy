package com.financy.financy.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseComparisonDto {
    private Double currentMonthExpenses;
    private Double previousMonthExpenses;
    private Double difference;
    private Double percentageChange;
    private String trend; // "increase", "decrease", or "no-change"
}
