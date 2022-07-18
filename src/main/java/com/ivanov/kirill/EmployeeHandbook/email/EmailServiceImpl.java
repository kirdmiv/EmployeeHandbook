package com.ivanov.kirill.EmployeeHandbook.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {
    @Autowired
    private MailSender mailSender;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Async
    public void sendMailMessage(String to, String event, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@EmployeeHandbook.com");
        message.setTo(to);
        message.setSubject(event);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    @Async
    public <T> void sendMailEntity(String to, String event, T entity) {
        String content;
        try {
            content = objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException jsonProcessingException) {
            content = jsonProcessingException.getMessage();
        }

        sendMailMessage(to, event, content);
    }
}
