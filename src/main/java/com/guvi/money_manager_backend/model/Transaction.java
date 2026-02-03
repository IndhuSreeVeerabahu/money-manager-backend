package com.guvi.money_manager_backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    private String id;

    private TransactionType type;

    private Double amount;

    private String category;

    private Division division;

    private String description;

    private LocalDateTime dateTime;

    private LocalDateTime createdAt;

    private String account;

    private String transferId;
}