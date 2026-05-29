package uk.version1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.version1.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByName(final String name);
}
