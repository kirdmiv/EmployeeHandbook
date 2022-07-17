package com.ivanov.kirill.EmployeeHandbook.controller;

import com.ivanov.kirill.EmployeeHandbook.dto.OrganizationDto;
import com.ivanov.kirill.EmployeeHandbook.model.Organization;
import com.ivanov.kirill.EmployeeHandbook.repository.OrganizationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/find")
    @ResponseBody
    public List<OrganizationDto> organizations(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String headQuarters
    ) {
        ExampleMatcher organizationMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("id");
        Example<Organization> query = Example.of(
                modelMapper.map(new OrganizationDto(null, title, null, null, headQuarters, null), Organization.class),
                organizationMatcher
        );
        return organizationRepository
                .findAll(query)
                .stream()
                .map(organization -> modelMapper.map(organization, OrganizationDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/findById")
    @ResponseBody
    public Optional<OrganizationDto> findOrganizationById(
            @RequestParam Long id
    ) {
        return organizationRepository
                .findById(id)
                .map(organization -> modelMapper.map(organization, OrganizationDto.class));
    }

    @PostMapping("/delete")
    public void deleteOrganizationById(
            @RequestParam Long id
    ) {
        organizationRepository.deleteById(id);
    }
}
