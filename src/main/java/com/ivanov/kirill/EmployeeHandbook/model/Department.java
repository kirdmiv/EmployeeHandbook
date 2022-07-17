package com.ivanov.kirill.EmployeeHandbook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("department")
public class Department extends Unit {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "department")
    private List<Team> teams;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @NotNull
    private Organization organization;
}
