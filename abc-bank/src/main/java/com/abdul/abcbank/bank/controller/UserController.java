package com.abdul.abcbank.bank.controller;

import com.abdul.abcbank.bank.dto.*;
import com.abdul.abcbank.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "abc-bank/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/createAccount")
    public Response createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping(path = "/balanceEnquiry")
    public Response balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping(path = "/accountEnquiry")
    public Response accountEnquiry(@RequestBody NameEnquiry nameEnquiry) {
        return userService.accountEnquiry(nameEnquiry);
    }

    @PostMapping(path = "/credit")
    public Response creditAlert(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.creditAccount(creditDebitRequest);
    }

    @PostMapping(path = "/debit")
    public Response debitAlert(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.DebitAccount(creditDebitRequest);
    }
    @PostMapping(path = "/transfer")
    public Response transfer(@RequestBody TransferRequest transferRequest) {
        return userService.Transfer(transferRequest);
    }
}
