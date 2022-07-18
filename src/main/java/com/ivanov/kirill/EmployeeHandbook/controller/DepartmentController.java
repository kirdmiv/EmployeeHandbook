package com.ivanov.kirill.EmployeeHandbook.controller;

import com.ivanov.kirill.EmployeeHandbook.dto.DepartmentDto;
import com.ivanov.kirill.EmployeeHandbook.model.Department;
import com.ivanov.kirill.EmployeeHandbook.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

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
        return departmentService
                .getMatchingDepartments(query)
                .stream()
                .map(department -> modelMapper.map(department, DepartmentDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> findDepartmentById(
            @PathVariable Long id
    ) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        return department.map(
                value -> ResponseEntity.ok(modelMapper.map(value, DepartmentDto.class))
        ).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteDepartmentById(
            @RequestParam Long id
    ) {
        if (departmentService.deleteDepartment(id))
            return ResponseEntity.ok("Successful");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid id");
    }

    @PostMapping("/add")
    public ResponseEntity<DepartmentDto> addDepartment(
            @RequestBody DepartmentDto departmentRequest
    ) {
        Department department = modelMapper.map(departmentRequest, Department.class);
        Optional<Department> addedDepartment = departmentService.addDepartment(department);
        return addedDepartment.map(
                value -> ResponseEntity.ok(modelMapper.map(value, DepartmentDto.class))
        ).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentDto departmentRequest
    ) {
        Department department = modelMapper.map(departmentRequest, Department.class);
        Optional<Department> updatedDepartment = departmentService.updateDepartment(id, department);
        return updatedDepartment.map(
                value -> ResponseEntity.ok(modelMapper.map(value, DepartmentDto.class))
        ).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }
}
