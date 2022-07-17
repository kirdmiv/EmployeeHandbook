package com.ivanov.kirill.EmployeeHandbook.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class TeamDto extends UnitDto implements Serializable {
    private List<EmployeeDto> employees;
    @JsonIgnore
    private DepartmentDto department;

    public TeamDto(Long id, String title, String description, EmployeeDto head, List<EmployeeDto> employees, DepartmentDto department) {
        super(id, title, description, head);
        this.employees = employees;
        this.department = department;
    }
}
