package com.ivanov.kirill.EmployeeHandbook.service;

import com.ivanov.kirill.EmployeeHandbook.model.Department;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<Department> getMatchingDepartments(Example<Department> departmentExample, Pageable rules);

    Optional<Department> getDepartmentById(Long id);

    Boolean deleteDepartment(Long id);

    Optional<Department> addDepartment(Department department);

    Optional<Department> updateDepartment(Long id, Department department);
}
