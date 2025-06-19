package uk.version1.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.context.web.WebAppConfiguration;

import org.springframework.test.web.servlet.MockMvc;
import uk.version1.model.Employee;
import uk.version1.service.EmployeeService;
import uk.version1.tags.FastTest;


import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = EmployeeApi.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = EmployeeApi.class )
@WebAppConfiguration
@Tag("Unit Test")
@FastTest
public class EmployeeApiTest {
    @MockitoBean
    private EmployeeService employeeService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;


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
            verify(employeeService).addEmployee(employeeArgumentCaptor.capture());
            assertThat(employeeArgumentCaptor.getValue())
                    .extracting("id","name","timeStamp")
                    .isEqualTo(Arrays.asList(1L,"John",LocalDateTime.of(1,1,1,1,1)));
        }
    }

    @Nested
    class GetAllEmployees{
        @DisplayName("should get all employees")
        @Test
        public void createEmployee_should_create_employee() throws Exception {
            //arrange
            var emp= Employee.builder().id(1L).name("John").timeStamp(LocalDateTime.now()).build();
            when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(emp));
            //act
            var actualResults= mockMvc.perform(get("/get")
                            .accept(MediaType.APPLICATION_JSON))
                             .andExpect(status().isOk())
                          .andReturn();
            //assert
            verify(employeeService).getAllEmployees();
            assertThat(actualResults.getResponse().getContentAsString()).isEqualTo(Collections.singletonList(objectMapper.writeValueAsString(emp)).toString());
        }
    }
}
