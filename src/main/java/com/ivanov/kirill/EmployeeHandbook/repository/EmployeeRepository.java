package com.ivanov.kirill.EmployeeHandbook.repository;

import com.ivanov.kirill.EmployeeHandbook.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
