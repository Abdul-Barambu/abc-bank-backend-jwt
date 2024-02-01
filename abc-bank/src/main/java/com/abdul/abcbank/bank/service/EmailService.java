package com.abdul.abcbank.bank.service;

import com.abdul.abcbank.bank.dto.EmailRequest;

public interface EmailService {

    void sendEmailAlert(EmailRequest emailRequest);
    void sendEmailAlertWithAttachment(EmailRequest emailRequest);
}
