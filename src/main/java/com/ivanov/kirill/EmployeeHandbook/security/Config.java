package com.ivanov.kirill.EmployeeHandbook.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class Config {
    @Bean
    public UserDetailServiceImpl userDetailService() {
        return new UserDetailServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();

        return http.build();
    }
}
