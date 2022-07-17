package com.ivanov.kirill.EmployeeHandbook.controller;

import com.ivanov.kirill.EmployeeHandbook.dto.EmployeeDto;
import com.ivanov.kirill.EmployeeHandbook.model.Employee;
import com.ivanov.kirill.EmployeeHandbook.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

    @GetMapping("/find")
    @ResponseBody
    public List<EmployeeDto> employees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email
    ) {
        ExampleMatcher employeeMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("id");
        Example<Employee> query = Example.of(
                modelMapper.map(new EmployeeDto(null, name, surname, email, null, null), Employee.class),
                employeeMatcher
        );
        return employeeService
                .getMatchingEmployees(query)
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Optional<EmployeeDto> findEmployeeById(
            @PathVariable Long id
    ) {
        return employeeService
                .getEmployeeById(id)
                .map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    @PostMapping("/delete")
    public void deleteEmployeeById(
            @RequestParam Long id
    ) {
        employeeService.deleteEmployee(id);
    }
}
