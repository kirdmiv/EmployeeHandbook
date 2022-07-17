package com.ivanov.kirill.EmployeeHandbook.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "teams")
@Getter
@Setter
@RequiredArgsConstructor
@DiscriminatorValue("team")
public class Team extends Unit {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "workplace")
    private List<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @NotNull
    private Department department;
}
