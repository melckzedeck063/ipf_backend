package com.zedeck.ipftestscenario.controllers;


import com.zedeck.ipftestscenario.dtos.EmployeeDto;
import com.zedeck.ipftestscenario.models.Employee;
import com.zedeck.ipftestscenario.services.EmployeeService;
import com.zedeck.ipftestscenario.utils.Response;
import com.zedeck.ipftestscenario.utils.ResponseCode;
import com.zedeck.ipftestscenario.utils.exceptions.GlobalException;
import com.zedeck.ipftestscenario.utils.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDto employeeDto) {
        Response<Employee> response = employeeService.createEmployee(employeeDto);
        if (response == null) {
            throw new GlobalException(ResponseCode.FAIL, "Request failed");
        }
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable(value = "id") Long id, @RequestBody EmployeeDto employeeDto) {
        Response<Employee> response = employeeService.updateEmployee(employeeDto);
        if (response == null) {
            throw new GlobalException(ResponseCode.FAIL, "Request failed");
        }

        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("id") Long employeeId) {
        Response<Employee> response = employeeService.getEmployeeById(employeeId);
        if (response == null) {
            throw new GlobalException(ResponseCode.FAIL, "Request failed");
        }

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllEmployees(@RequestParam(name = "page", defaultValue = "0")Integer page,
                                             @RequestParam(name = "size", defaultValue = "10")Integer size) {
        PageRequest pageRequest = PageRequest.of(page,size);

        Page<Employee> employeePage =  employeeService.getEmployees(pageRequest);
        if (employeePage == null) {
            throw new GlobalException(ResponseCode.FAIL, "Request failed");
        }

        return ResponseEntity.ok().body(employeePage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long employeeId) {
        Response<Employee> response = employeeService.deleteEmployee(employeeId);
        if (response == null) {
            throw new GlobalException(ResponseCode.FAIL, "Request failed");
        }
        return ResponseEntity.ok().body(response);
    }


}
