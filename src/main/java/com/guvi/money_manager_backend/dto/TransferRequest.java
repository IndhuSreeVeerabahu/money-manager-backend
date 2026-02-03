package com.guvi.money_manager_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {

    private String fromAccount;
    private String toAccount;
    private double amount;
    private String description;
}
