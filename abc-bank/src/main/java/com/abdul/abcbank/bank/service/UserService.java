package com.abdul.abcbank.bank.service;

import com.abdul.abcbank.bank.dto.Response;
import com.abdul.abcbank.bank.dto.UserRequest;

public interface UserService {

    Response createAccount(UserRequest userRequest);
}
