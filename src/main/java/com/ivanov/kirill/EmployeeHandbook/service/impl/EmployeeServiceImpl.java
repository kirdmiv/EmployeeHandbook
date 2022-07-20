package com.ivanov.kirill.EmployeeHandbook.service.impl;

import com.ivanov.kirill.EmployeeHandbook.email.EmailService;
import com.ivanov.kirill.EmployeeHandbook.model.Employee;
import com.ivanov.kirill.EmployeeHandbook.model.Unit;
import com.ivanov.kirill.EmployeeHandbook.model.UserRole;
import com.ivanov.kirill.EmployeeHandbook.repository.EmployeeRepository;
import com.ivanov.kirill.EmployeeHandbook.repository.UnitRepository;
import com.ivanov.kirill.EmployeeHandbook.service.EmployeeService;
import com.ivanov.kirill.EmployeeHandbook.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public List<Employee> getMatchingEmployees(Example<Employee> employeeExample, Pageable rules) {
        return employeeRepository.findAll(employeeExample, rules).toList();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Boolean deleteEmployee(Long id) {
        try {
            employeeRepository.deleteById(id);
            return true;
        } catch (DataAccessException exception) {
            return false;
        }
    }

    @Override
    public Optional<Employee> addEmployee(Employee employee, Long unitId) {
        if (unitId != null) {
            Optional<Unit> workplace = unitRepository.findById(unitId);
            if (workplace.isPresent())
                employee.setWorkplace(workplace.get());
            else
                return Optional.empty();
        }

        if (employee.getRole() == null)
            employee.setRole(UserRole.ROLE_USER);

        employee.setUsername(PasswordGenerator.generate());
        employee.setPassword(passwordEncoder.encode(PasswordGenerator.generate()));
        employee = employeeRepository.save(employee);

        String username = employee.getSurname() + '_' + employee.getId();
        String password = PasswordGenerator.generate();

        employee.setUsername(username);
        employee.setPassword(passwordEncoder.encode(password));

        employee = employeeRepository.save(employee);
        emailService.sendLoginInfo(employee.getEmail(), username, password);

        return Optional.of(employee);
    }

    @Override
    public Optional<Employee> updateEmployee(Long id, Employee employee, Long unitId) {
        Optional<Employee> employeeFromDB = employeeRepository.findById(id);
        if (!employeeFromDB.isPresent())
            return Optional.empty();

        employee.setId(employeeFromDB.get().getId());

        return Optional.of(employeeRepository.save(employee));
    }
}
