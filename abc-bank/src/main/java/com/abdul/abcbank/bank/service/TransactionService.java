package com.abdul.abcbank.bank.service;

import com.abdul.abcbank.bank.dto.TransactionRequest;

public interface TransactionService {

    void saveTransaction(TransactionRequest transactionRequest);
}
