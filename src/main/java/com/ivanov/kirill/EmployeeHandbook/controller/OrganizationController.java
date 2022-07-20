package com.ivanov.kirill.EmployeeHandbook.controller;

import com.ivanov.kirill.EmployeeHandbook.dto.OrganizationDto;
import com.ivanov.kirill.EmployeeHandbook.email.EmailService;
import com.ivanov.kirill.EmployeeHandbook.model.Organization;
import com.ivanov.kirill.EmployeeHandbook.service.OrganizationService;
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
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;
    
    @GetMapping("/find")
    @ResponseBody
    public List<OrganizationDto> organizations(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "0", name = "pgNum") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "100", name = "pgSize") Integer pageSize,
            @RequestParam(required = false, defaultValue = "title", name = "sort") String sortByField
    ) {
        ExampleMatcher organizationMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("id");
        Example<Organization> query = Example.of(
                modelMapper.map(new OrganizationDto(null, title, null, null, null, null), Organization.class),
                organizationMatcher
        );
        Pageable rules = PageRequest.of(pageNumber, pageSize, Sort.by(sortByField));
        return organizationService
                .getMatchingOrganizations(query, rules)
                .stream()
                .map(organization -> modelMapper.map(organization, OrganizationDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> findOrganizationById(
            @PathVariable Long id
    ) {
        Optional<Organization> organization = organizationService.getOrganizationById(id);
        return organization.map(
                value -> ResponseEntity.ok(modelMapper.map(value, OrganizationDto.class))
        ).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteOrganizationById(
            @RequestParam Long id
    ) {
        Optional<Organization> organization = organizationService.getOrganizationById(id);
        if (!organization.isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid id");
        if (!AuthorizeUser.checkOrganizationAccess(organization.get(), SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not enough permissions");

        if (organizationService.deleteOrganization(id)) {
            emailService.sendMailMessage(
                    "kirdmiv@gmail.com",
                    "Organization deleted",
                    "Organization with id=" + id + " has been deleted successfully."
            );
            return ResponseEntity.ok("Successful");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid id");
    }

    @PostMapping("/add")
    public ResponseEntity<OrganizationDto> addOrganization(
            @RequestBody OrganizationDto organizationRequest
    ) {
        Organization organization = modelMapper.map(organizationRequest, Organization.class);
        Optional<Organization> addedOrganization = organizationService.addOrganization(organization);
        if (addedOrganization.isPresent()) {
            OrganizationDto addedOrganizationDto = modelMapper.map(addedOrganization.get(), OrganizationDto.class);
            emailService.sendMailEntity(
                    "kirdmiv@gmail.com",
                    "Organization added",
                    addedOrganizationDto
            );
            return ResponseEntity.ok(addedOrganizationDto);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<OrganizationDto> updateOrganization(
            @PathVariable Long id,
            @RequestBody OrganizationDto organizationRequest
    ) {
        Optional<Organization> organization = organizationService.getOrganizationById(id);
        if (!organization.isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (!AuthorizeUser.checkOrganizationAccess(organization.get(), SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        Organization newOrganization = modelMapper.map(organizationRequest, Organization.class);
        Optional<Organization> updatedOrganization = organizationService.updateOrganization(id, newOrganization);
        if (updatedOrganization.isPresent()) {
            OrganizationDto updatedOrganizationDto = modelMapper.map(updatedOrganization.get(), OrganizationDto.class);
            emailService.sendMailEntity(
                    "kirdmiv@gmail.com",
                    "Organization updated",
                    updatedOrganizationDto
            );
            return ResponseEntity.ok(updatedOrganizationDto);
        }
        return ResponseEntity.badRequest().body(null);
    }
}
