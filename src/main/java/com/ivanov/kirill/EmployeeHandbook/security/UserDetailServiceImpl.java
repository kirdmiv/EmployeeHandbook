package com.ivanov.kirill.EmployeeHandbook.security;

import com.ivanov.kirill.EmployeeHandbook.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserPrincipal(employeeRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employee with username: " + username + "has not been found.")));
    }
}
