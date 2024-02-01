package com.abdul.abcbank.bank.controller;

import com.abdul.abcbank.bank.entity.Transaction;
import com.abdul.abcbank.bank.service.imple.BankStatement;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/getStatement")
@RequiredArgsConstructor
public class TransactionController {

    private final BankStatement bankStatement;

    @GetMapping
    public List<Transaction> getBankStatement(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }
}
