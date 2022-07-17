package com.ivanov.kirill.EmployeeHandbook.service;

import com.ivanov.kirill.EmployeeHandbook.model.Employee;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getMatchingEmployees(Example<Employee> employeeExample);

    Optional<Employee> getEmployeeById(long id);

    void deleteEmployee(long id);

    Employee addEmployee(Employee employee);

    Employee updateEmployee(long id, Employee employee);
}
