package com.abdul.abcbank.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {

    private String transactionType;
    private String accountNumber;
    private String accountName;
    private BigDecimal amount;
    private BigDecimal balance;
    private LocalDate date;
    private LocalTime time;
    private String status;
}
