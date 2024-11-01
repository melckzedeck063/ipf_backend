package com.zedeck.ipftestscenario;

import com.zedeck.ipftestscenario.config.TestSecurityConfig;
import com.zedeck.ipftestscenario.dtos.EmployeeDto;
import com.zedeck.ipftestscenario.models.Employee;
import com.zedeck.ipftestscenario.repositories.EmployeeRepository;
import com.zedeck.ipftestscenario.utils.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
//@Transactional
//@Import(TestSecurityConfig.class)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;


    private ObjectMapper objectMapper= new ObjectMapper();

    private EmployeeDto employeeDto;

    @BeforeEach
    public void setup() {
        employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Martin");
        employeeDto.setLastName("Odergard");
        employeeDto.setEmail("odegard@arsenal.com");
        employeeDto.setDepartment("Sports");
        employeeDto.setSalary(5550000.0);
    }

    @Test
    public void createEmployeeTest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(employeeDto);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.responseCode").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Employee created successfully"));
    }

    @Test
    public void getEmployeeByIdTest() throws Exception {
        Employee savedEmployee = employeeRepository.save(Employee.builder()
                .firstName("Melck")
                .lastName("Zedeck")
                .email("zedeck@test.com")
                .department("Engineering")
                .salary(4300000)
                .build()
        );

        mockMvc.perform(get("http://localhost:9092/api/employees/{id}", savedEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.firstName").value("Melck"))
                .andExpect(jsonPath("$.data.email").value("zedeck@test.com"));
    }
    @Test
    public void updateEmployeeTest() throws Exception {
        Employee savedEmployee = employeeRepository.save(Employee.builder()
                .firstName("Melck")
                .lastName("Zedeck")
                .email("zedeck@test.com")
                .department("Engineering")
                .salary(300000)
                .build()
        );
        employeeDto.setFirstName("Bukayo");
        employeeDto.setLastName("Saka");
        employeeDto.setSalary(360000.0);

        String jsonRequest = objectMapper.writeValueAsString(employeeDto);

        mockMvc.perform(put("http://localhost:9092/api/employees/{id}", savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.firstName").value("Michael"))
                .andExpect(jsonPath("$.data.salary").value(60000.0));
    }

    @Test
    public void deleteEmployeeTest() throws Exception {
        Employee savedEmployee = employeeRepository.save(Employee.builder()
                .firstName("Melck")
                .lastName("Zedeck")
                .email("zedeck@test.com")
                .department("Engineering")
                .salary(300000)
                .build()
        );

        mockMvc.perform(delete("http://localhost:9092/api/employees/{id}", savedEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("Employee deleted successfully"));

        Optional<Employee> deletedEmployee = employeeRepository.findById(savedEmployee.getId());
        assertThat(deletedEmployee.isPresent()).isFalse();
    }

    @Test
    public void getAllEmployeesTest() throws Exception {
        employeeRepository.save(Employee.builder()
                .firstName("Agness")
                .lastName("Kajobi")
                .email("agness@test.com")
                .department("Engineering")
                .salary(450000)
                .build()
        );
        employeeRepository.save(Employee.builder()
                .firstName("Mumbara")
                .lastName("Munubi")
                .email("mnubi@test.com")
                .department("Engineering")
                .salary(6300000)
                .build()
        );

        MvcResult mvcResult = mockMvc.perform(get("http://localhost:9092/api/employees"))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response.contains("Agness")).isTrue();
        assertThat(response.contains("Mumbara")).isTrue();
    }
}
