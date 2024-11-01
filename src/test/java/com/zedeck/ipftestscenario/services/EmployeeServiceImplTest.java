package com.zedeck.ipftestscenario.services;

import com.zedeck.ipftestscenario.dtos.EmployeeDto;
import com.zedeck.ipftestscenario.models.Employee;
import com.zedeck.ipftestscenario.models.UserAccount;
import com.zedeck.ipftestscenario.repositories.EmployeeRepository;
import com.zedeck.ipftestscenario.servicesImpl.EmployeeServiceImpl;
import com.zedeck.ipftestscenario.utils.Response;
import com.zedeck.ipftestscenario.utils.ResponseCode;
import com.zedeck.ipftestscenario.utils.userextractor.LoggedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LoggedUser loggedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void createEmployeeWhenUserIsLoggedIn() {
        UserAccount user = new UserAccount();
        when(loggedUser.getUser()).thenReturn(user);

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("melckzedeck");
        employeeDto.setLastName("james");
        employeeDto.setEmail("melckzedeck@test.com");
        employeeDto.setDepartment("Engineering");
        employeeDto.setSalary(50000);

        when(employeeRepository.findFirstByEmail(employeeDto.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(new Employee());

        Response<Employee> response = employeeService.createEmployee(employeeDto);

        assertNotNull(response);
        assertTrue(response.isSuccess(),"Employee creation failed");
        assertEquals(ResponseCode.SUCCESS, response.getCode());
        assertEquals("Employee created successfully", response.getMessage());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void createEmployeeWhenUserIsNotLoggedIn() {
        when(loggedUser.getUser()).thenReturn(null);
        EmployeeDto employeeDto = new EmployeeDto();
        Response<Employee> response = employeeService.createEmployee(employeeDto);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ResponseCode.UNAUTHORIZED, response.getCode());
        assertEquals("Unauthorized", response.getMessage());
    }

    @Test
    public void updateEmployeeWhenExists() {
        UserAccount user = new UserAccount();
        when(loggedUser.getUser()).thenReturn(user);

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(1L);
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setEmail("john.doe@example.com");
        employeeDto.setDepartment("Engineering");
        employeeDto.setSalary(50000);

        Employee existingEmployee = new Employee();
        when(employeeRepository.findById(employeeDto.getEmployeeId())).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);
        Response<Employee> response = employeeService.updateEmployee(employeeDto);
        assertNotNull(response);
        assertTrue(response.isSuccess(),"Employee update failed");
        assertEquals(ResponseCode.SUCCESS, response.getCode());
        assertEquals("Employee updated successfully", response.getMessage());
        verify(employeeRepository, times(1)).save(existingEmployee);
    }


    @Test
    public void deleteEmployeeWhenEmployeeExist() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Response<Employee> response = employeeService.deleteEmployee(employeeId);

        assertNotNull(response);
        assertTrue(response.isSuccess(), "Employee deletion failed");
        assertEquals(ResponseCode.SUCCESS, response.getCode());
        assertEquals("Employee deleted successfully", response.getMessage());

        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void deleteEmployeeWhenEmployeeDoesNotExist() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Response<Employee> response = employeeService.deleteEmployee(employeeId);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ResponseCode.NULL_ARGUMENT, response.getCode());
        assertEquals("employee not found", response.getMessage());
    }


    @Test
    public void getEmployeeByIdWhenEmployeeExist() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Response<Employee> response = employeeService.getEmployeeById(employeeId);

        assertNotNull(response);
        assertTrue(response.isSuccess(), "Employee get failed");
        assertEquals(ResponseCode.SUCCESS, response.getCode());
        assertEquals("Employee found successfully", response.getMessage());
        assertEquals(employee, response.getData());
    }

    @Test
    public void getEmployeeByIdWhenEmployeeDoesNotExist() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Response<Employee> response = employeeService.getEmployeeById(employeeId);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ResponseCode.NULL_ARGUMENT, response.getCode());
        assertEquals("employee not found", response.getMessage());
    }

    @Test
    public void getAllEmployeesWhenEmployeeExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Employee employee = new Employee();
        Page<Employee> employeePage = new PageImpl<>(Collections.singletonList(employee));

        when(employeeRepository.findAllByDeletedFalse(pageable)).thenReturn(employeePage);

        Page<Employee> result = employeeService.getEmployees(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(employee, result.getContent().get(0));

        verify(employeeRepository, times(1)).findAllByDeletedFalse(pageable);
    }


}
