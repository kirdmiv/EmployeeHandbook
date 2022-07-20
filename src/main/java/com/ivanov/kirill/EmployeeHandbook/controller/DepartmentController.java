package com.ivanov.kirill.EmployeeHandbook.controller;

import com.ivanov.kirill.EmployeeHandbook.dto.DepartmentDto;
import com.ivanov.kirill.EmployeeHandbook.email.EmailService;
import com.ivanov.kirill.EmployeeHandbook.model.Department;
import com.ivanov.kirill.EmployeeHandbook.service.DepartmentService;
import com.ivanov.kirill.EmployeeHandbook.utils.AuthorizeUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private EmailService emailService;

    @GetMapping("/find")
    @ResponseBody
    public List<DepartmentDto> departments(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "0", name = "pgNum") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "100", name = "pgSize") Integer pageSize,
            @RequestParam(required = false, defaultValue = "title", name = "sort") String sortByField
    ) {
        ExampleMatcher departmentMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("id");
        Example<Department> query = Example.of(
                modelMapper.map(new DepartmentDto(null, title, null, null, null, null), Department.class),
                departmentMatcher
        );
        Pageable rules = PageRequest.of(pageNumber, pageSize, Sort.by(sortByField));
        return departmentService
                .getMatchingDepartments(query, rules)
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
        Optional<Department> department = departmentService.getDepartmentById(id);
        if (!department.isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid id");
        if (!AuthorizeUser.checkDepartmentAccess(department.get(), SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not enough permissions");
        
        if (departmentService.deleteDepartment(id)) {
            emailService.sendMailMessage(
                    "kirdmiv@gmail.com",
                    "Department deleted",
                    "Department with id=" + id + " has been deleted successfully."
            );
            return ResponseEntity.ok("Successful");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid id");
    }

    @PostMapping("/add")
    public ResponseEntity<DepartmentDto> addDepartment(
            @RequestBody DepartmentDto departmentRequest
    ) {
        Department department = modelMapper.map(departmentRequest, Department.class);
        Optional<Department> addedDepartment = departmentService.addDepartment(department);
        if (addedDepartment.isPresent()) {
            DepartmentDto addedDepartmentDto = modelMapper.map(addedDepartment.get(), DepartmentDto.class);
            emailService.sendMailEntity(
                    "kirdmiv@gmail.com",
                    "Department added",
                    addedDepartmentDto
            );
            return ResponseEntity.ok(addedDepartmentDto);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentDto departmentRequest
    ) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        if (!department.isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (!AuthorizeUser.checkDepartmentAccess(department.get(), SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        Department newDepartment = modelMapper.map(departmentRequest, Department.class);
        Optional<Department> updatedDepartment = departmentService.updateDepartment(id, newDepartment);if (updatedDepartment.isPresent()) {
            DepartmentDto updatedDepartmentDto = modelMapper.map(updatedDepartment.get(), DepartmentDto.class);
            emailService.sendMailEntity(
                    "kirdmiv@gmail.com",
                    "Department updated",
                    updatedDepartmentDto
            );
            return ResponseEntity.ok(updatedDepartmentDto);
        }
        return ResponseEntity.badRequest().body(null);
    }
}
