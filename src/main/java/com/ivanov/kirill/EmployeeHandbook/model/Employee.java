package com.ivanov.kirill.EmployeeHandbook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "employees")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "surname")
    @NotBlank
    private String surname;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @ManyToOne
    @JoinColumn(name = "workplace_id")
    private Unit workplace;
}
