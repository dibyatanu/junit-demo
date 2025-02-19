package uk.version1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.version1.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
