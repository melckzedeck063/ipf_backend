package com.zedeck.ipftestscenario.servicesImpl;

import com.zedeck.ipftestscenario.dtos.EmployeeDto;
import com.zedeck.ipftestscenario.models.Employee;
import com.zedeck.ipftestscenario.models.UserAccount;
import com.zedeck.ipftestscenario.repositories.EmployeeRepository;
import com.zedeck.ipftestscenario.services.EmployeeService;
import com.zedeck.ipftestscenario.utils.Response;
import com.zedeck.ipftestscenario.utils.ResponseCode;
import com.zedeck.ipftestscenario.utils.userextractor.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LoggedUser loggedUser;

    @Override
    public Response<Employee> createEmployee(EmployeeDto employeeDto) {
        try {
            UserAccount user = loggedUser.getUser();
            if(user == null) {
                return new Response<>(true,ResponseCode.UNAUTHORIZED,"Unauthorized");
            }

            Employee employee = new Employee();

            if(employeeDto.getFirstName() == null || employeeDto.getLastName() == null) {
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"firstname or lastname is null");
            }
            if(employeeDto.getEmail() == null) {
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"email is null");
            } else if (employeeDto.getEmail().isBlank()) {
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"email is blank");
            }
            else  {
                if(isValidEmail(employeeDto.getEmail())) {
                    Optional<Employee> employeeOptional =  employeeRepository.findFirstByEmail(employeeDto.getEmail());

                    if(employeeOptional.isPresent()) {
                        return new Response<>(true,ResponseCode.UNAUTHORIZED,"Provided email already  exist");
                    }

                    if(!employeeDto.getEmail().isBlank() && !Objects.equals(employeeDto.getEmail(), employee.getEmail())){
                       employee.setEmail(employeeDto.getEmail());
                    }
                }
                else {
                    return new Response<>(true,ResponseCode.BAD_REQUEST,"Invalid email");
                }
            }

            if(employeeDto.getDepartment() == null) {
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"department is null");
            }
            if (employeeDto.getSalary() < 0){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"salary can not be negative");
            }
            else {
                employee.setSalary(employeeDto.getSalary());
            }

            if(!employeeDto.getFirstName().isBlank() && !Objects.equals(employeeDto.getFirstName(), employee.getFirstName())){
                employee.setFirstName(employeeDto.getFirstName());
            }
            if(!employeeDto.getLastName().isBlank() && !Objects.equals(employeeDto.getLastName(), employee.getLastName())){
                employee.setLastName(employeeDto.getLastName());
            }


            if(!employeeDto.getDepartment().isBlank() && !Objects.equals(employeeDto.getDepartment(), employee.getDepartment())) {
                employee.setDepartment(employeeDto.getDepartment());
            }


            Employee employee1 =  employeeRepository.save(employee);

            return new Response<>(true,ResponseCode.SUCCESS,employee1, "Employee created successfully");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new Response<>(true, ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Employee> updateEmployee(EmployeeDto employeeDto) {
        try {
            UserAccount user =  loggedUser.getUser();
            if (user == null) {
                return new Response<>(true,ResponseCode.UNAUTHORIZED,"Unauthorized");
            }
            if(employeeDto.getEmployeeId() == null  || employeeDto.getEmployeeId() < 0){
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"employee id is null");
            }
            else {
                Optional<Employee> employee = employeeRepository.findById(employeeDto.getEmployeeId());
                if(employee.isPresent()){
                    Employee employee1 =  employee.get();

                    employee1.setFirstName(employeeDto.getFirstName());
                    employee1.setLastName(employeeDto.getLastName());
                    employee1.setEmail(employeeDto.getEmail());
                    employee1.setDepartment(employeeDto.getDepartment());
                    employee1.setSalary(employeeDto.getSalary());

                    Employee employee2 =  employeeRepository.save(employee1);
                    return new Response<>(true,ResponseCode.SUCCESS,employee2, "Employee updated successfully");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response<Employee> deleteEmployee(Long employeeId) {
        try {
            if(employeeId == null)
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"employee id is null");

            Optional<Employee> employee = employeeRepository.findById(employeeId);

            if(employee.isPresent()) {
                Employee employee1 =  employee.get();
                employee1.setDeleted(true);
                employeeRepository.save(employee1);

                return new Response<>(true, ResponseCode.SUCCESS, "Employee deleted successfully");
            }
            else {
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"employee not found");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true, ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Employee> getEmployeeById(Long employeeId) {
        try {
            if(employeeId == null)
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"employee id is null");

            Optional<Employee> employee = employeeRepository.findById(employeeId);

            if(employee.isPresent()) {
                Employee employee1 =  employee.get();
                return new Response<>(true, ResponseCode.SUCCESS,employee1, "Employee found successfully");
            }
            else {
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"employee not found");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true, ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Page<Employee> getEmployees(Pageable pageable) {
        try {
            Page<Employee> employees = employeeRepository.findAllByDeletedFalse(pageable);

            return employees;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new PageImpl<>(new ArrayList<>());
    }



    private boolean isValidEmail(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


}
