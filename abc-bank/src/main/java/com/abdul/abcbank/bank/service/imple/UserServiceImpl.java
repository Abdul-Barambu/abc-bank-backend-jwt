package com.abdul.abcbank.bank.service.imple;

import com.abdul.abcbank.bank.accountNumber.AccountNumber;
import com.abdul.abcbank.bank.dto.AccountInfo;
import com.abdul.abcbank.bank.dto.EmailRequest;
import com.abdul.abcbank.bank.dto.Response;
import com.abdul.abcbank.bank.dto.UserRequest;
import com.abdul.abcbank.bank.entity.Role;
import com.abdul.abcbank.bank.entity.User;
import com.abdul.abcbank.bank.repository.UserRepository;
import com.abdul.abcbank.bank.service.EmailService;
import com.abdul.abcbank.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

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
}
