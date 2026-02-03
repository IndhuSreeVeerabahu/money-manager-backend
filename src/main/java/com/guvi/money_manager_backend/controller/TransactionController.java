package com.guvi.money_manager_backend.controller;

import com.guvi.money_manager_backend.dto.CategorySummary;
import com.guvi.money_manager_backend.dto.IncomeExpenseSummary;
import com.guvi.money_manager_backend.dto.TransferRequest;
import com.guvi.money_manager_backend.model.Division;
import com.guvi.money_manager_backend.model.Transaction;
import com.guvi.money_manager_backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;

    // Add income or expense
    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.addTransaction(transaction));
    }

    // Get all transactions
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    // Update transaction (within 12 hours)
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable String id,
            @RequestBody Transaction transaction) {

        return ResponseEntity.ok(
                transactionService.updateTransaction(id, transaction)
        );
    }

    @GetMapping("/division/{division}")
    public ResponseEntity<List<Transaction>> getByDivision(
            @PathVariable Division division) {

        return ResponseEntity.ok(
                transactionService.getByDivision(division)
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Transaction>> getByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(
                transactionService.getByCategory(category)
        );
    }

    @GetMapping("/between-dates")
    public ResponseEntity<List<Transaction>> getBetweenDates(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {

        return ResponseEntity.ok(
                transactionService.getBetweenDates(start, end)
        );
    }

    @GetMapping("/summary/weekly")
    public ResponseEntity<IncomeExpenseSummary> weeklySummary() {
        return ResponseEntity.ok(transactionService.getWeeklySummary());
    }

    @GetMapping("/summary/monthly")
    public ResponseEntity<IncomeExpenseSummary> monthlySummary() {
        return ResponseEntity.ok(transactionService.getMonthlySummary());
    }

    @GetMapping("/summary/yearly")
    public ResponseEntity<IncomeExpenseSummary> yearlySummary() {
        return ResponseEntity.ok(transactionService.getYearlySummary());
    }

    @GetMapping("/summary/category")
    public ResponseEntity<List<CategorySummary>> categorySummary() {
        return ResponseEntity.ok(transactionService.getCategorySummary());
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        transactionService.transferAmount(request);
        return ResponseEntity.ok("Transfer completed successfully");
    }






}
