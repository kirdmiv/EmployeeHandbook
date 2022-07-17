package com.ivanov.kirill.EmployeeHandbook.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DepartmentDto extends UnitDto implements Serializable {
    private List<TeamDto> teams;
    @JsonIgnore
    private OrganizationDto organization;

    public DepartmentDto(Long id, String title, String description, EmployeeDto head, List<TeamDto> teams, OrganizationDto organization) {
        super(id, title, description, head);
        this.teams = teams;
        this.organization = organization;
    }
}
