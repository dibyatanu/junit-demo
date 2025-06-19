package uk.version1.repository;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import uk.version1.helper.DbTestContainers;
import uk.version1.model.Employee;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ImportTestcontainers(DbTestContainers.class)
public class EmployeeRepositoryTest  {
    @Autowired
    private EmployeeRepository employeeRepository;

    @DisplayName("Should get employee by name")
    @Test
    public void findByName(){
        employeeRepository.save(Employee.builder().name("John").timeStamp(LocalDateTime.of(1,1,1,1,1)).build());

        var employee= employeeRepository.findByName("John");

       assertThat(employee).isNotEmpty();

    }
}
