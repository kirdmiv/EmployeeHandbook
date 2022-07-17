package com.ivanov.kirill.EmployeeHandbook.repository;

import com.ivanov.kirill.EmployeeHandbook.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
