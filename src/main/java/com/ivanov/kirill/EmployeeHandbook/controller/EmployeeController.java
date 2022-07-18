package com.ivanov.kirill.EmployeeHandbook.controller;

import com.ivanov.kirill.EmployeeHandbook.dto.EmployeeDto;
import com.ivanov.kirill.EmployeeHandbook.email.EmailService;
import com.ivanov.kirill.EmployeeHandbook.model.Employee;
import com.ivanov.kirill.EmployeeHandbook.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    @GetMapping("/find")
    @ResponseBody
    public List<EmployeeDto> employees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false, defaultValue = "0", name = "pgNum") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "100", name = "pgSize") Integer pageSize,
            @RequestParam(required = false, defaultValue = "name", name = "sort") String sortByField
    ) {
        ExampleMatcher employeeMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("id");
        Example<Employee> query = Example.of(
                modelMapper.map(new EmployeeDto(null, name, surname, email, null, null), Employee.class),
                employeeMatcher
        );
        Pageable rules = PageRequest.of(pageNumber, pageSize, Sort.by(sortByField));
        return employeeService
                .getMatchingEmployees(query, rules)
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> findEmployeeById(
            @PathVariable Long id
    ) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(
                value -> ResponseEntity.ok(modelMapper.map(value, EmployeeDto.class))
        ).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteEmployeeById(
            @RequestParam Long id
    ) {
        if (employeeService.deleteEmployee(id)) {
            emailService.sendMailMessage(
                    "kirdmiv@gmail.com",
                    "Employee deleted",
                    "Employee with id=" + id + " has been deleted successfully."
            );
            return ResponseEntity.ok("Successful");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid id");
    }

    @PostMapping("/add")
    public ResponseEntity<EmployeeDto> addEmployee(
            @RequestParam(required = false) Long unitId,
            @RequestBody EmployeeDto employeeRequest
    ) {
        Employee employee = modelMapper.map(employeeRequest, Employee.class);
        Optional<Employee> addedEmployee = employeeService.addEmployee(employee, unitId);
        if (addedEmployee.isPresent()) {
            EmployeeDto addedEmployeeDto = modelMapper.map(addedEmployee.get(), EmployeeDto.class);
            emailService.sendMailEntity(
                    "kirdmiv@gmail.com",
                    "Employee added",
                    addedEmployeeDto
            );
            return ResponseEntity.ok(addedEmployeeDto);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable Long id,
            @RequestParam(required = false) Long unitId,
            @RequestBody EmployeeDto employeeRequest
    ) {
        Employee employee = modelMapper.map(employeeRequest, Employee.class);
        Optional<Employee> updatedEmployee = employeeService.updateEmployee(id, employee, unitId);
        if (updatedEmployee.isPresent()) {
            EmployeeDto updatedEmployeeDto = modelMapper.map(updatedEmployee.get(), EmployeeDto.class);
            emailService.sendMailEntity(
                    "kirdmiv@gmail.com",
                    "Employee updated",
                    updatedEmployeeDto
            );
            return ResponseEntity.ok(updatedEmployeeDto);
        }
        return ResponseEntity.badRequest().body(null);
    }
}
