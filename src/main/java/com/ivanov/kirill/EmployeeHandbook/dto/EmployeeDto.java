package com.ivanov.kirill.EmployeeHandbook.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeDto implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private Date birthday;
    private UnitDto workplace;
}
