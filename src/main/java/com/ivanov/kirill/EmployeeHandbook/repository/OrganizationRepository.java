package com.ivanov.kirill.EmployeeHandbook.repository;

import com.ivanov.kirill.EmployeeHandbook.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
