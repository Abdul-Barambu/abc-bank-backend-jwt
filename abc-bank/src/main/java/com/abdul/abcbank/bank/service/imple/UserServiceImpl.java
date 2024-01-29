package com.abdul.abcbank.bank.service.imple;

import com.abdul.abcbank.bank.accountNumber.AccountNumber;
import com.abdul.abcbank.bank.dto.*;
import com.abdul.abcbank.bank.entity.Role;
import com.abdul.abcbank.bank.entity.Transaction;
import com.abdul.abcbank.bank.entity.User;
import com.abdul.abcbank.bank.repository.UserRepository;
import com.abdul.abcbank.bank.service.EmailService;
import com.abdul.abcbank.bank.service.TransactionService;
import com.abdul.abcbank.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TransactionService transactionService;

    @Override
    public Response createAccount(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            Response response = Response.builder()
                    .code("001")
                    .message("User already exits")
                    .accountInfo(null)
                    .build();

            return response;
        }

        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .phoneNumber(userRequest.getPhoneNumber())
                .accountNumber(AccountNumber.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .state(userRequest.getState())
                .status("ACTIVE")
                .role(Role.USER)
                .build();

        User saveUser = userRepository.save(user);

        String accountCreationMessage = "Hello " + saveUser.getName() + "!!! \nYour account has been created successfully! " +
                "\nHere is your account details below. . . \nAccount name: " + saveUser.getName() + "\nAccount number: " + saveUser.getAccountNumber()
                + "\nThank you for choosing ABC Bank";

//        Email
        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(saveUser.getEmail())
                .subject("ACCOUNT CREATION")
                .message(accountCreationMessage)
                .build();

        emailService.sendEmailAlert(emailRequest);

        Response response = Response.builder()
                .code("002")
                .message("Account created Successfully")
                .accountInfo(AccountInfo.builder()
                        .accountName(saveUser.getName())
                        .accountNumber(saveUser.getAccountNumber())
                        .accountBalance(saveUser.getAccountBalance())
                        .build())
                .build();

        return response;
    }


    //    find balance
    @Override
    public Response balanceEnquiry(EnquiryRequest enquiryRequest) {

        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist) {
            Response response = Response.builder()
                    .code("003")
                    .message("Account Does not exists")
                    .accountInfo(null)
                    .build();

            return response;
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        Response response = Response.builder()
                .code("004")
                .message("Balance fetched successfully")
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(foundUser.getEmail())
                .subject("BALANCE REQUEST")
                .message("Your account balance is = " + foundUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(emailRequest);

        return response;
    }

    //    find account
    @Override
    public Response accountEnquiry(NameEnquiry nameEnquiry) {
        boolean isNameExist = userRepository.existsByName(nameEnquiry.getName());
        if (!isNameExist) {
            Response response = Response.builder()
                    .code("005")
                    .message("Name Does not exists")
                    .accountInfo(null)
                    .build();

            return response;
        }

        User foundUser = userRepository.findByName(nameEnquiry.getName());
        Response response = Response.builder()
                .code("006")
                .message("Account number fetched successfully")
                .accountInfo(AccountInfo.builder()
                        .accountNumber(foundUser.getAccountNumber())
                        .build())
                .build();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(foundUser.getEmail())
                .subject("ACCOUNT NUMBER REQUEST")
                .message("Your account number is: " + foundUser.getAccountNumber())
                .build();

        emailService.sendEmailAlert(emailRequest);

        return response;
    }


    //    credit alert
    @Override
    public Response creditAccount(CreditDebitRequest creditDebitRequest) {

        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());

        if (!isAccountExist) {
            Response response = Response.builder()
                    .code("003")
                    .message("Account Does not exists")
                    .accountInfo(null)
                    .build();

            return response;
        }

        User accountToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        accountToCredit.setAccountBalance(accountToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));

        userRepository.save(accountToCredit);

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .transactionType("CREDIT")
                .accountName(accountToCredit.getName())
                .accountNumber(accountToCredit.getAccountNumber())
                .amount(creditDebitRequest.getAmount())
                .balance(accountToCredit.getAccountBalance())
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        transactionService.saveTransaction(transactionRequest);

        Response response = Response.builder()
                .code("007")
                .message("Account credited")
                .accountInfo(AccountInfo.builder()
                        .accountName(accountToCredit.getName())
                        .accountNumber(accountToCredit.getAccountNumber())
                        .accountBalance(accountToCredit.getAccountBalance())
                        .build())
                .build();

        String emailMessage = "Credit! \nAcc:" + creditDebitRequest.getAccountNumber() + "\nAmt:₦" + creditDebitRequest.getAmount() +
                "\nTID: 0003872539 \nDate:" + LocalDateTime.now() + "\nBal:₦" + accountToCredit.getAccountBalance();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(accountToCredit.getEmail())
                .subject("CREDIT ALERT")
                .message(emailMessage)
                .build();

        emailService.sendEmailAlert(emailRequest);

        return response;
    }

    //    Debit alert
    @Override
    public Response DebitAccount(CreditDebitRequest creditDebitRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());

        if (!isAccountExist) {
            Response response = Response.builder()
                    .code("003")
                    .message("Account Does not exists")
                    .accountInfo(null)
                    .build();

            return response;
        }

        User debitAccount = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
//        check if amount is greater than balance
        if(debitAccount.getAccountBalance().compareTo(creditDebitRequest.getAmount()) < 0){
            Response response = Response.builder()
                    .code("008")
                    .message("Insufficient Balance")
                    .accountInfo(null)
                    .build();

            return response;
        }else {
            debitAccount.setAccountBalance(debitAccount.getAccountBalance().subtract(creditDebitRequest.getAmount()));
            userRepository.save(debitAccount);

            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .transactionType("DEBIT")
                    .accountName(debitAccount.getName())
                    .accountNumber(debitAccount.getAccountNumber())
                    .amount(creditDebitRequest.getAmount())
                    .balance(debitAccount.getAccountBalance())
                    .date(LocalDate.now())
                    .time(LocalTime.now())
                    .build();
            transactionService.saveTransaction(transactionRequest);
        }

        Response response = Response.builder()
                .code("009")
                .message("Debit alert")
                .accountInfo(AccountInfo.builder()
                        .accountName(debitAccount.getName())
                        .accountNumber(debitAccount.getAccountNumber())
                        .accountBalance(debitAccount.getAccountBalance())
                        .build())
                .build();

        String emailMessage = "Debit! \nAcc:" + creditDebitRequest.getAccountNumber() + "\nAmt:₦" + creditDebitRequest.getAmount() +
                "\nTID: 0003872539 \nDate:" + LocalDateTime.now() + "\nBal:₦" + debitAccount.getAccountBalance();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(debitAccount.getEmail())
                .subject("DEBIT ALERT")
                .message(emailMessage)
                .build();

        emailService.sendEmailAlert(emailRequest);

        return response;
    }

//    transfer
    @Override
    public Response Transfer(TransferRequest transferRequest) {

        boolean isAccountToDebit = userRepository.existsByAccountNumber(transferRequest.getAccountFrom());
        boolean isAccountToCredit = userRepository.existsByAccountNumber(transferRequest.getAccountTo());

        if(!isAccountToDebit || !isAccountToCredit){
            Response response = Response.builder()
                    .code("010")
                    .message("Account does not exist")
                    .build();

            return response;
        }
        User accountToDebit = userRepository.findByAccountNumber(transferRequest.getAccountFrom());
        if(accountToDebit.getAccountBalance().compareTo(transferRequest.getAmount()) < 0){
            Response response = Response.builder()
                    .code("011")
                    .message("Insufficient Balance")
                    .accountInfo(null)
                    .build();

            return response;
        }else {
            accountToDebit.setAccountBalance(accountToDebit.getAccountBalance().subtract(transferRequest.getAmount()));
            userRepository.save(accountToDebit);

            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .transactionType("DEBIT TRANSFER To "+transferRequest.getAccountTo())
                    .accountName(accountToDebit.getName())
                    .accountNumber(transferRequest.getAccountFrom())
                    .amount(transferRequest.getAmount())
                    .balance(accountToDebit.getAccountBalance())
                    .date(LocalDate.now())
                    .time(LocalTime.now())
                    .build();
            transactionService.saveTransaction(transactionRequest);

            String emailMessageDebit = "Debit! \nAcc:" + transferRequest.getAccountFrom() + "\nAmt:₦" + transferRequest.getAmount() +
                    "\nTID: 0003872539 \nDate:" + LocalDateTime.now() + "\nBal:₦" + accountToDebit.getAccountBalance();

//        mail debit
            EmailRequest emailRequest = EmailRequest.builder()
                    .recipient(accountToDebit.getEmail())
                    .subject("DEBIT")
                    .message(emailMessageDebit)
                    .build();

            emailService.sendEmailAlert(emailRequest);
        }

        if(!isAccountToDebit || !isAccountToCredit){
            Response response = Response.builder()
                    .code("010")
                    .message("Account does not exist")
                    .build();

            return response;
        }else {
            User accountToCredit = userRepository.findByAccountNumber(transferRequest.getAccountTo());
            accountToCredit.setAccountBalance(accountToCredit.getAccountBalance().add(transferRequest.getAmount()));
            userRepository.save(accountToCredit);

            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .transactionType("CREDIT TRANSFER From "+transferRequest.getAccountFrom())
                    .accountName(accountToCredit.getName())
                    .accountNumber(transferRequest.getAccountTo())
                    .amount(transferRequest.getAmount())
                    .balance(accountToDebit.getAccountBalance())
                    .date(LocalDate.now())
                    .time(LocalTime.now())
                    .build();
            transactionService.saveTransaction(transactionRequest);

            String emailMessageCredit = "CREDIT! \nAcc:" + transferRequest.getAccountTo() + "\nAmt:₦" + transferRequest.getAmount() +
                    "\nTID: 0003872539 \nDate:" + LocalDateTime.now() + "\nBal:₦" + accountToCredit.getAccountBalance();

//        mail credit
            EmailRequest emailRequest = EmailRequest.builder()
                    .recipient(accountToCredit.getEmail())
                    .subject("CREDIT")
                    .message(emailMessageCredit)
                    .build();

            emailService.sendEmailAlert(emailRequest);
        }

        Response response = Response.builder()
                .code("012")
                .message("Transfer successfully")
                .build();
            return response;

    }
}
