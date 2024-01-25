package com.abdul.abcbank.bank.controller;

import com.abdul.abcbank.bank.dto.Response;
import com.abdul.abcbank.bank.dto.UserRequest;
import com.abdul.abcbank.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "abc-bank/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/createAccount")
    public Response createAccount(@RequestBody UserRequest userRequest){
       return userService.createAccount(userRequest);
    }
}
