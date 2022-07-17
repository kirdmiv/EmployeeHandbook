package com.ivanov.kirill.EmployeeHandbook.service.impl;

import com.ivanov.kirill.EmployeeHandbook.model.Employee;
import com.ivanov.kirill.EmployeeHandbook.repository.EmployeeRepository;
import com.ivanov.kirill.EmployeeHandbook.repository.UnitRepository;
import com.ivanov.kirill.EmployeeHandbook.service.EmployeeService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UnitRepository unitRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UnitRepository unitRepository) {
        this.employeeRepository = employeeRepository;
        this.unitRepository = unitRepository;
    }

    @Override
    public List<Employee> getMatchingEmployees(Example<Employee> employeeExample) {
        return employeeRepository.findAll(employeeExample);
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee addEmployee(Employee employee, Long unitId) {
        if (unitId != null)
            employee.setWorkplace(unitRepository.findById(unitId).orElse(null));
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(long id, Employee employee) {
        return null;
    }
}
