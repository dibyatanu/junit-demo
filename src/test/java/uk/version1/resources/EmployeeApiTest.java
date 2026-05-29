package uk.version1.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;



import org.springframework.test.web.servlet.MockMvc;
import uk.version1.model.Employee;
import uk.version1.service.EmployeeService;
import uk.version1.tags.FastTest;


import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmployeeApi.class)
@Tag("Unit Test")
@FastTest
public class EmployeeApiTest {
    @MockitoBean
    private EmployeeService mockEmployeeService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> employeeNameCaptor;


    @Nested
    class CreateEmployee{
        @DisplayName("should call employee service")
        @Test
        public void createEmployee_should_create_employee() throws Exception {
            //arrange
           var emp= Employee.builder().id(1L).name("John").timeStamp(LocalDateTime.of(1,1,1,1,1)).build();
           //act
           mockMvc.perform(post("/add")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(objectMapper.writeValueAsString(emp)))
                         .andExpect(status().isCreated());
            //assert
            verify(mockEmployeeService).addEmployee(employeeArgumentCaptor.capture());
            assertThat(employeeArgumentCaptor.getValue())
                    .extracting("name","timeStamp")
                    .isEqualTo(Arrays.asList("John",LocalDateTime.of(1,1,1,1,1)));
        }
    }

    @Nested
    class GetAllEmployees{
        @DisplayName("should get all employees")
        @Test
        public void createEmployee_should_create_employee() throws Exception {
            //arrange
            var emp= Employee.builder().id(1L).name("John").timeStamp(LocalDateTime.now()).build();
            when(mockEmployeeService.getAllEmployees()).thenReturn(Collections.singletonList(emp));
            //act
            var actualResults= mockMvc.perform(get("/get")
                              .contentType(MediaType.APPLICATION_JSON))
                              .andExpect(status().isOk())
                              .andReturn();
            //assert
            verify(mockEmployeeService).getAllEmployees();
            assertThat(actualResults.getResponse().getContentAsString()).isEqualTo(Collections.singletonList(objectMapper.writeValueAsString(emp)).toString());
        }
    }

    @Nested
    class GetEmployeeByName{

        @DisplayName("Should return employee by name")
        @Test
        public void getEmployeeByName_should_return_employee() throws Exception {
            var emp= Employee.builder().id(1L).name("John").timeStamp(LocalDateTime.now()).build();
            when(mockEmployeeService.findEmployeeByName(eq("John"))).thenReturn(emp);

            var actualResults= mockMvc.perform(get("/getemployeebyname")
                             .param("name","John")
                             .contentType(MediaType.APPLICATION_JSON))
                             .andExpect(status().isOk())
                             .andReturn();
            verify(mockEmployeeService).findEmployeeByName(employeeNameCaptor.capture());
            assertThat(employeeNameCaptor.getValue()).isEqualTo("John");
            assertThat(actualResults.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(emp));
        }

        @DisplayName("Should throw exception when employee name not found")
        @Test
        public void getEmployeeByName_throws_exception() throws Exception {
            when(mockEmployeeService.findEmployeeByName(anyString())).thenThrow( new EntityNotFoundException("Employee name: unknown not found"));


            mockMvc.perform(get("/getemployeebyname")
                                 .param("name","unknown")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()))
                                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("Employee name: unknown not found"));

        }
    }
}
