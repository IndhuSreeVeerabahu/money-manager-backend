package com.guvi.money_manager_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IncomeExpenseSummary {

    private double totalIncome;
    private double totalExpense;
}
