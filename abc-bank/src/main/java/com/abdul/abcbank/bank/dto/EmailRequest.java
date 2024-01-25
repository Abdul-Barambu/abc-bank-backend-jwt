package com.abdul.abcbank.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {

    private String recipient;
    private String subject;
    private String message;
    private String attachment;
}
