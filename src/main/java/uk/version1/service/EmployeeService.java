package uk.version1.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uk.version1.model.Employee;
import uk.version1.repository.EmployeeRepository;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    public void addEmployee(final Employee employee) {
        employeeRepository.saveAndFlush(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll() ;
    }


    public Employee findEmployeeByName(final String employeeName) {
        return employeeRepository.findByName(employeeName).orElseThrow(()->
                                 new  EntityNotFoundException(String.format("Employee name: %s not found",employeeName)));
    }
}