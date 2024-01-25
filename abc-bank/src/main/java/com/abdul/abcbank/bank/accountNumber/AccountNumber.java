package com.abdul.abcbank.bank.accountNumber;

public class AccountNumber {

    public static String generateAccountNumber() {
        int constant = 0;
        int min = 10000000; // 10 digits
        int max = 99999999;  // 10 digits

        int randomNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String constantNumber = String.format("%02d", constant);
        String randomNum = String.valueOf(randomNumber);

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(constantNumber).append(randomNum).toString();
    }
}
