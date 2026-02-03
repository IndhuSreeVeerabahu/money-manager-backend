package com.guvi.money_manager_backend.repository;

import com.guvi.money_manager_backend.model.Transaction;
import com.guvi.money_manager_backend.model.TransactionType;
import com.guvi.money_manager_backend.model.Division;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    // Filter by type (INCOME / EXPENSE)
    List<Transaction> findByType(TransactionType type);

    // Filter by category
    List<Transaction> findByCategory(String category);

    // Filter by division (OFFICE / PERSONAL)
    List<Transaction> findByDivision(Division division);

    // Filter between two dates
    List<Transaction> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
