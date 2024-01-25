package com.abdul.abcbank.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserRequest {

    private String name;
    private String gender;
    private String address;
    private String state;
    private String email;
//    private String password;
    private String phoneNumber;
}
