package com.financy.financy.transaction.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.financy.financy.transaction.entity.Transaction;
import com.financy.financy.transaction.entity.TransactionType;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);

    List<Transaction> findByUserIdAndTypeAndCreatedAtBetween(Long userId, TransactionType type, LocalDateTime startDate, LocalDateTime endDate);
    
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long id);
    
    void deleteById(Long id);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type")
    Double getTotalByUserIdAndType(@Param("userId") Long userId, @Param("type") TransactionType type);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
    Double getTotalByUserIdAndTypeAndMonth(@Param("userId") Long userId, @Param("type") TransactionType type, @Param("year") int year, @Param("month") int month);

    default Double getTotalExpensesByUserId(Long userId) {
        return getTotalByUserIdAndType(userId, TransactionType.EXPENSE);
    }

    default Double getTotalIncomeByUserId(Long userId) {
        return getTotalByUserIdAndType(userId, TransactionType.INCOME);
    }

    default Double getCurrentMonthIncomeByUserId(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return getTotalByUserIdAndTypeAndMonth(userId, TransactionType.INCOME, now.getYear(), now.getMonthValue());
    }

    default Double getPreviousMonthIncomeByUserId(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime previousMonth = now.minusMonths(1);
        return getTotalByUserIdAndTypeAndMonth(userId, TransactionType.INCOME, previousMonth.getYear(), previousMonth.getMonthValue());
    }

    default Double getCurrentMonthExpensesByUserId(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return getTotalByUserIdAndTypeAndMonth(userId, TransactionType.EXPENSE, now.getYear(), now.getMonthValue());
    }

    default Double getPreviousMonthExpensesByUserId(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime previousMonth = now.minusMonths(1);
        return getTotalByUserIdAndTypeAndMonth(userId, TransactionType.EXPENSE, previousMonth.getYear(), previousMonth.getMonthValue());
    }
}
