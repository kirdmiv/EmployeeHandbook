package com.ivanov.kirill.EmployeeHandbook.email;

public interface EmailService {
    void sendMail(String to, String event, String content);
}
