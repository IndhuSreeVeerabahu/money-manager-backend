package com.guvi.money_manager_backend.service;

import com.guvi.money_manager_backend.model.Transaction;
import com.guvi.money_manager_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    // Add income or expense
    public Transaction addTransaction(Transaction transaction) {
        transaction.setCreatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Edit transaction within 12 hours
    public Transaction updateTransaction(String id, Transaction updatedTransaction) {

        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        long hours = Duration.between(existing.getCreatedAt(), LocalDateTime.now()).toHours();

        if (hours > 12) {
            throw new RuntimeException("Editing not allowed after 12 hours");
        }

        existing.setAmount(updatedTransaction.getAmount());
        existing.setCategory(updatedTransaction.getCategory());
        existing.setDivision(updatedTransaction.getDivision());
        existing.setDescription(updatedTransaction.getDescription());
        existing.setDateTime(updatedTransaction.getDateTime());
        existing.setAccount(updatedTransaction.getAccount());

        return transactionRepository.save(existing);
    }
}
