package com.ivanov.kirill.EmployeeHandbook.controller;

import com.ivanov.kirill.EmployeeHandbook.dto.OrganizationDto;
import com.ivanov.kirill.EmployeeHandbook.model.Organization;
import com.ivanov.kirill.EmployeeHandbook.service.OrganizationService;
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
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/find")
    @ResponseBody
    public List<OrganizationDto> organizations(
            @RequestParam(required = false) String title
    ) {
        ExampleMatcher organizationMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("id");
        Example<Organization> query = Example.of(
                modelMapper.map(new OrganizationDto(null, title, null, null, null, null), Organization.class),
                organizationMatcher
        );
        return organizationService
                .getMatchingOrganizations(query)
                .stream()
                .map(organization -> modelMapper.map(organization, OrganizationDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> findOrganizationById(
            @PathVariable Long id
    ) {
        Optional<Organization> organization = organizationService.getOrganizationById(id);
        if (organization.isPresent())
            return ResponseEntity.ok(modelMapper.map(organization.get(), OrganizationDto.class));
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteOrganizationById(
            @RequestParam Long id
    ) {
        if (organizationService.deleteOrganization(id))
            return ResponseEntity.ok("Successful");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid id");
    }

    @PostMapping("/add")
    public ResponseEntity<OrganizationDto> addOrganization(
            @RequestBody OrganizationDto organizationRequest
    ) {
        Organization organization = modelMapper.map(organizationRequest, Organization.class);
        Optional<Organization> addedOrganization = organizationService.addOrganization(organization);
        if (addedOrganization.isPresent())
            return ResponseEntity.ok(modelMapper.map(addedOrganization.get(), OrganizationDto.class));
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<OrganizationDto> updateOrganization(
            @PathVariable Long id,
            @RequestBody OrganizationDto organizationRequest
    ) {
        Organization organization = modelMapper.map(organizationRequest, Organization.class);
        Optional<Organization> updatedOrganization = organizationService.updateOrganization(id, organization);
        if (updatedOrganization.isPresent())
            return ResponseEntity.ok(modelMapper.map(updatedOrganization.get(), OrganizationDto.class));
        return ResponseEntity.badRequest().body(null);
    }
}
