package com.abdul.abcbank.bank.service.imple;

import com.abdul.abcbank.bank.dto.EmailRequest;
import com.abdul.abcbank.bank.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendEmailAlert(EmailRequest emailRequest) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(emailRequest.getRecipient());
            simpleMailMessage.setSubject(emailRequest.getSubject());
            simpleMailMessage.setText(emailRequest.getMessage());

            javaMailSender.send(simpleMailMessage);

        }catch (MailException e){
            throw new RuntimeException(e);
        }
    }
}
