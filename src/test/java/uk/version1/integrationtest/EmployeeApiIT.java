package uk.version1.integrationtest;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.PostgreSQLContainer;
import uk.version1.model.Employee;
import uk.version1.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql( "classpath:test_data/delete_employee.sql")
@Tag("Integration Test")
public class EmployeeApiIT {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository employeeRepository;
    private final  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );
    @BeforeAll
    public static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    public static void afterAll() {
        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    private static void configureProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
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
                         .expectStatus().isOk();
           //assert
           var employeeList=  employeeRepository.findAll();
           assertThat(employeeList).extracting("name","timeStamp")
                      .containsAll(List.of(tuple("John", LocalDateTime.of(1,1,1,1,1))));
            assertThat(employeeList.size()).isEqualTo(1);
        }
    }

    @Nested
    @Sql("classpath:test_data/create_employee.sql")
    class GetEmployee{
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
}
