package com.ivanov.kirill.EmployeeHandbook.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {
    @Autowired
    private MailSender mailSender;

    @Override
    public void sendMail(String to, String event, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@EmployeeHandbook.com");
        message.setTo(to);
        message.setSubject(event);
        message.setText(content);
        mailSender.send(message);
    }
}
