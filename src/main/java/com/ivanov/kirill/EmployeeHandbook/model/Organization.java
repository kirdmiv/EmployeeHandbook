package com.ivanov.kirill.EmployeeHandbook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("organization")
public class Organization extends Unit {
    @Column(name = "head_quarters")
    private String headQuarters;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "organization")
    private List<Department> departments;
}
