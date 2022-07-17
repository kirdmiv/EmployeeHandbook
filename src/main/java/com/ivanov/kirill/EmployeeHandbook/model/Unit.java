package com.ivanov.kirill.EmployeeHandbook.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "units")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "unit_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@RequiredArgsConstructor
public abstract class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    @NotBlank
    protected String title;

    @Column(name = "description")
    protected String description;

    @OneToOne
    @JoinColumn(name = "head_id")
    protected Employee head;
}
