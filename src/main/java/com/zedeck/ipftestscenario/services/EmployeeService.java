package com.zedeck.ipftestscenario.services;

import com.zedeck.ipftestscenario.dtos.EmployeeDto;
import com.zedeck.ipftestscenario.models.Employee;
import com.zedeck.ipftestscenario.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {

    Response<Employee> createEmployee(EmployeeDto employeeDto);

    Response<Employee> updateEmployee(EmployeeDto employeeDto);

    Response<Employee> deleteEmployee(Long employeeId);

    Response<Employee> getEmployeeById(Long employeeId);

    Page<Employee> getEmployees(Pageable pageable);
}
