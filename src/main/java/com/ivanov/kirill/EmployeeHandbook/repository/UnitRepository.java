package com.ivanov.kirill.EmployeeHandbook.repository;

import com.ivanov.kirill.EmployeeHandbook.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
}
