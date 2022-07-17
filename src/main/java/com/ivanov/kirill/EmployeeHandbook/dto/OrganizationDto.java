package com.ivanov.kirill.EmployeeHandbook.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class OrganizationDto extends UnitDto implements Serializable {
    private String headQuarters;
    private List<DepartmentDto> departments;

    public OrganizationDto(Long id, String title, String description, EmployeeDto head, String headQuarters, List<DepartmentDto> departments) {
        super(id, title, description, head);
        this.headQuarters = headQuarters;
        this.departments = departments;
    }
}
