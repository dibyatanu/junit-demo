package uk.version1.resources;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.version1.model.Employee;
import uk.version1.service.EmployeeService;



import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public final class EmployeeApi {
    private  EmployeeService employeeService;

    @Operation(summary = "Creates Employee")
    @ApiResponses(value = {@ApiResponse(responseCode = "201")})
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createEmployee(@RequestBody final Employee employee){
        log.info("Creating Employee");
        employeeService.addEmployee(employee);
    }

    @Operation(summary = "Get All Employees")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping(value = "/get" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAllEmployee(){
        log.info("Getting Employee");
        return employeeService.getAllEmployees();
    }
}
