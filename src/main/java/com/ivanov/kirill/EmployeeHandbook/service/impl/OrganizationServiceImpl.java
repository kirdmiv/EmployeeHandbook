package com.ivanov.kirill.EmployeeHandbook.service.impl;

import com.ivanov.kirill.EmployeeHandbook.model.Organization;
import com.ivanov.kirill.EmployeeHandbook.repository.OrganizationRepository;
import com.ivanov.kirill.EmployeeHandbook.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public List<Organization> getMatchingOrganizations(Example<Organization> organizationExample, Pageable rules) {
        return organizationRepository.findAll(organizationExample, rules).toList();
    }

    @Override
    public Optional<Organization> getOrganizationById(Long id) {
        return organizationRepository.findById(id);
    }

    @Override
    public Boolean deleteOrganization(Long id) {
        try {
            organizationRepository.deleteById(id);
            return true;
        } catch (DataAccessException exception) {
            return false;
        }
    }

    @Override
    public Optional<Organization> addOrganization(Organization organization) {
        return Optional.of(organizationRepository.save(organization));
    }

    @Override
    public Optional<Organization> updateOrganization(Long id, Organization organization) {
        Optional<Organization> organizationFromDB = organizationRepository.findById(id);
        if (!organizationFromDB.isPresent())
            return Optional.empty();

        organization.setId(organizationFromDB.get().getId());

        return Optional.of(organizationRepository.save(organization));
    }
}
