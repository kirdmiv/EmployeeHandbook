package com.ivanov.kirill.EmployeeHandbook.service;

import com.ivanov.kirill.EmployeeHandbook.model.Employee;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getMatchingEmployees(Example<Employee> employeeExample, Pageable rules);

    Optional<Employee> getEmployeeById(Long id);

    Boolean deleteEmployee(Long id);

    Optional<Employee> addEmployee(Employee employee, Long organizationId);

    Optional<Employee> updateEmployee(Long id, Employee employee, Long unitId);
}
