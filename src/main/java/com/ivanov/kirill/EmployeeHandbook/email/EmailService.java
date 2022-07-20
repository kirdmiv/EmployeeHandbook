package com.ivanov.kirill.EmployeeHandbook.email;

public interface EmailService {
    void sendMailMessage(String to, String event, String content);
    <T> void sendMailEntity(String to, String event, T entity);
    void sendLoginInfo(String to, String login, String password);
}
