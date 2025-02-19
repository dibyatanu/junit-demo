package uk.version1.resources;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.version1.model.Employee;
import uk.version1.service.EmployeeService;



import java.util.List;

@RestController
@AllArgsConstructor
public final class EmployeeApi {
    private  EmployeeService employeeService;


    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createEmployee(@RequestBody final Employee employee){
        employeeService.addEmployee(employee);
    }

    @GetMapping(value = "/get" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAllEmployee(){
      return employeeService.getAllEmployees();
    }
}
