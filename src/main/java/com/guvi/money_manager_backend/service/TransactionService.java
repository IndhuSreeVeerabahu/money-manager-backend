package com.guvi.money_manager_backend.service;

import com.guvi.money_manager_backend.dto.CategorySummary;
import com.guvi.money_manager_backend.dto.IncomeExpenseSummary;
import com.guvi.money_manager_backend.dto.TransferRequest;
import com.guvi.money_manager_backend.model.Division;
import com.guvi.money_manager_backend.model.Transaction;
import com.guvi.money_manager_backend.model.TransactionType;
import com.guvi.money_manager_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    // Filter by division
    public List<Transaction> getByDivision(Division division) {
        return transactionRepository.findByDivision(division);
    }

    // Filter by category
    public List<Transaction> getByCategory(String category) {
        return transactionRepository.findByCategory(category);
    }

    // Filter between two dates
    public List<Transaction> getBetweenDates(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByDateTimeBetween(start, end);
    }

    public IncomeExpenseSummary getWeeklySummary() {

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(7);

        return calculateSummary(start, end);
    }
    private IncomeExpenseSummary calculateSummary(
            LocalDateTime start,
            LocalDateTime end) {

        List<Transaction> transactions =
                transactionRepository.findByDateTimeBetween(start, end);

        double income = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        return new IncomeExpenseSummary(income, expense);
    }

    public IncomeExpenseSummary getMonthlySummary() {

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusMonths(1);

        return calculateSummary(start, end);
    }
    public IncomeExpenseSummary getYearlySummary() {

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusYears(1);

        return calculateSummary(start, end);
    }
    public List<CategorySummary> getCategorySummary() {

        List<Transaction> transactions = transactionRepository.findAll();

        Map<String, Double> categoryMap = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        return categoryMap.entrySet().stream()
                .map(e -> new CategorySummary(e.getKey(), e.getValue()))
                .toList();
    }

    public void transferAmount(TransferRequest request) {

        String transferId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        // Debit (Expense)
        Transaction debit = Transaction.builder()
                .type(TransactionType.EXPENSE)
                .amount(request.getAmount())
                .category("Transfer")
                .division(Division.PERSONAL)
                .description("Transfer to " + request.getToAccount())
                .account(request.getFromAccount())
                .dateTime(now)
                .createdAt(now)
                .transferId(transferId)
                .build();

        // Credit (Income)
        Transaction credit = Transaction.builder()
                .type(TransactionType.INCOME)
                .amount(request.getAmount())
                .category("Transfer")
                .division(Division.PERSONAL)
                .description("Transfer from " + request.getFromAccount())
                .account(request.getToAccount())
                .dateTime(now)
                .createdAt(now)
                .transferId(transferId)
                .build();

        transactionRepository.save(debit);
        transactionRepository.save(credit);
    }







}
