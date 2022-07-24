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
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };

    private static final String[] ADMIN_LIST = {
            "/api/**"
    };

    private static final String[] MODERATOR_LIST = {
            "/api/**/update/**",
            "/api/**/delete",
            "/api/teams/updateEmployees/**"
    };

    private static final String[] USER_LIST = {
            "/api/**/find",
            "/api/employees/employee/**",
            "/api/teams/team/**",
            "/api/departments/department/**",
            "/api/organizations/organization/**"
    };

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
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(USER_LIST).hasAnyRole("ADMIN", "MODERATOR", "USER")
                .antMatchers(MODERATOR_LIST).hasAnyRole("ADMIN", "MODERATOR")
                .antMatchers(ADMIN_LIST).hasRole("ADMIN")
                .and()
                .formLogin().permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();

        return http.build();
    }
}
