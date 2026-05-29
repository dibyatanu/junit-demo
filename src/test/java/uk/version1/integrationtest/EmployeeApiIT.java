package uk.version1.integrationtest;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.reactive.server.WebTestClient;
import uk.version1.helper.DbTestContainers;
import uk.version1.model.Employee;
import uk.version1.repository.EmployeeRepository;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(value = "classpath:test_data/delete_employee.sql", executionPhase = BEFORE_TEST_METHOD)
@Tag("Integration Test")
@ImportTestcontainers(DbTestContainers.class)
public class EmployeeApiIT {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Nested
    class CreateEmployee{
        @DisplayName("should create employee")
        @Test
        public void should_create_employee(){
            //act
            webTestClient.post()
                         .uri("/add")
                         .contentType(MediaType.APPLICATION_JSON)
                         .bodyValue(Employee.builder().name("John").timeStamp(LocalDateTime.of(1,1,1,1,1)).build())
                         .exchange()
                         .expectStatus().isCreated();
           //assert
           var employeeList=  employeeRepository.findAll();
           assertThat(employeeList).extracting("name","timeStamp")
                      .containsAll(List.of(tuple("John", LocalDateTime.of(1,1,1,1,1))));
            assertThat(employeeList.size()).isEqualTo(1);
        }
    }

    @Nested
    class GetAllEmployee{
        @Sql("classpath:test_data/create_employee.sql")
        @DisplayName("should get employee")
        @Test
        public void getEmployees(){
            //act
            var employees= webTestClient.get()
                    .uri("/get")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(Employee.class)
                    .returnResult().getResponseBody();
            //assert
            assertThat(employees).extracting("name","timeStamp")
                    .containsAll(List.of(tuple("John", LocalDateTime.of(1,1,1,1,1))));
            assertThat(employees.size()).isEqualTo(1);

        }
    }

    @Nested
    class GetEmployeeByName{
        @Sql("classpath:test_data/create_employee.sql")
        @DisplayName("Should get employee by name")
        @Test
        public void getEmployeeByName_returnsEmployee(){
              webTestClient.get()
                           .uri( uriBuilder ->
                                             uriBuilder.path("/getemployeebyname")
                                                     .queryParam("name","John").build())
                          .accept(MediaType.APPLICATION_JSON)
                          .exchange()
                          .expectStatus().isOk();
        }

        @Sql("classpath:test_data/create_employee.sql")
        @DisplayName("Should throw Excpetion when name not found")
        @Test
        public  void getEmployeeByName_ThrowsException(){
              webTestClient.get()
                      .uri( uriBuilder ->
                            uriBuilder.path("/getemployeebyname")
                                    .queryParam("name","unknown").build())
                         .accept(MediaType.APPLICATION_JSON)
                      .exchange()
                      .expectStatus().isNotFound();

        }
    }
}
