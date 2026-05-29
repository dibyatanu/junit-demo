package uk.version1.service;

import jakarta.persistence.EntityNotFoundException;
import mockit.MockUp;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.version1.model.Employee;
import uk.version1.repository.EmployeeRepository;
import uk.version1.tags.FastTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("Unit Test")
@FastTest
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    //SUT
    @InjectMocks
    private EmployeeService employeeService;
    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> employeeNameCaptor;
    @BeforeEach
     public void setUp(){
        new MockUp<LocalDateTime>(){
            @mockit.Mock
            public LocalDateTime now(){
                return LocalDateTime.of(1,1,1,1,1);
            }
        };

     }
    @Nested
    class AddEmployee{
        @DisplayName("Should call repository service")
        @Test
        public void addEmployee(){
            //arrange
            var emp= Employee.builder().id(1L).name("John").timeStamp(LocalDateTime.now()).build();
            //act
            employeeService.addEmployee(emp);
            //assert
            verify(employeeRepository).saveAndFlush(employeeArgumentCaptor.capture());
            assertThat(employeeArgumentCaptor.getValue())
                    .extracting("id","name","timeStamp")
                    .isEqualTo(Arrays.asList(1L,"John",LocalDateTime.now()));

        }
    }

    @Nested
    class GetAllEmployees{
        @DisplayName("Should get all employee")
        @Test
        public void getAllEmployees_should_get_all_employee(){
            //arrange
            var emp= Employee.builder().id(1L).name("John").timeStamp(LocalDateTime.now()).build();
            when(employeeRepository.findAll()).thenReturn(Collections.singletonList(emp));
            //act
            var actualResults= employeeService.getAllEmployees();
            //arrange
            verify(employeeRepository).findAll();
            assertThat(actualResults.size()).isEqualTo(1);

        }
    }

    @Nested
    class FindEmployeeByName{

        @DisplayName("Should return employee details by name")
        @Test
        public void findEmployeeByName(){
            var emp= Employee.builder().id(1L).name("John").timeStamp(LocalDateTime.now()).build();
            when(employeeRepository.findByName(eq("John"))).thenReturn(Optional.ofNullable(emp));

            var expectedEmployee=employeeService.findEmployeeByName("John");

            verify(employeeRepository).findByName(employeeNameCaptor.capture());
            assertThat(employeeNameCaptor.getValue()).isEqualTo("John");
            assertThat(expectedEmployee).isEqualTo(emp);
        }

        @DisplayName("Should throw exception when employee name not found")
        @Test
        public void findEmployeeByNameThrowException(){
            when(employeeRepository.findByName("unknown")).thenReturn(Optional.empty());

            var exception= Assertions.assertThrows(EntityNotFoundException.class,()->{
                           employeeService.findEmployeeByName("unknown");
                           });


            assertThat(exception.getMessage()).isEqualTo("Employee name: unknown not found");
        }
    }
}
