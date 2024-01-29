package com.abdul.abcbank.bank.service.imple;

import com.abdul.abcbank.bank.dto.TransactionRequest;
import com.abdul.abcbank.bank.entity.Transaction;
import com.abdul.abcbank.bank.repository.TransactionRepository;
import com.abdul.abcbank.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionRequest.getTransactionType())
                .accountName(transactionRequest.getAccountName())
                .accountNumber(transactionRequest.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .balance(transactionRequest.getBalance())
                .date(transactionRequest.getDate())
                .time(transactionRequest.getTime())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);

        System.out.println("Transaction Saved");
    }
}
