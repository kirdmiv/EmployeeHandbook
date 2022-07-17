package com.ivanov.kirill.EmployeeHandbook.dto;

import lombok.*;

import java.io.Serializable;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UnitDto implements Serializable {
    protected Long id;
    protected String title;
    protected String description;
    protected EmployeeDto head;
}
