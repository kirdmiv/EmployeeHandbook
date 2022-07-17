package com.ivanov.kirill.EmployeeHandbook.controller;

import com.ivanov.kirill.EmployeeHandbook.dto.DepartmentDto;
import com.ivanov.kirill.EmployeeHandbook.model.Department;
import com.ivanov.kirill.EmployeeHandbook.repository.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/find")
    @ResponseBody
    public List<DepartmentDto> departments(
            @RequestParam(required = false) String title
    ) {
        ExampleMatcher departmentMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("id");
        Example<Department> query = Example.of(
                modelMapper.map(new DepartmentDto(null, title, null, null, null, null), Department.class),
                departmentMatcher
        );
        return departmentRepository
                .findAll(query)
                .stream()
                .map(department -> modelMapper.map(department, DepartmentDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/findById")
    @ResponseBody
    public Optional<DepartmentDto> findDepartmentById(
            @RequestParam Long id
    ) {
        return departmentRepository
                .findById(id)
                .map(department -> modelMapper.map(department, DepartmentDto.class));
    }
}
