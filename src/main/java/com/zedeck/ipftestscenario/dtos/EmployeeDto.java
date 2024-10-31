package com.zedeck.ipftestscenario.dtos;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class EmployeeDto  {

    private String firstName;

    private String lastName;

    private String email;

    private String department;

    private double salary;

    private Long employeeId;

}
