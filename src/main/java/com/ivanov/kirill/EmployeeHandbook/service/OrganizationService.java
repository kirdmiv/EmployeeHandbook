package com.ivanov.kirill.EmployeeHandbook.service;

import com.ivanov.kirill.EmployeeHandbook.model.Organization;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    List<Organization> getMatchingOrganizations(Example<Organization> organizationExample, Pageable rules);

    Optional<Organization> getOrganizationById(Long id);

    Boolean deleteOrganization(Long id);

    Optional<Organization> addOrganization(Organization organization);

    Optional<Organization> updateOrganization(Long id, Organization organization);
}
