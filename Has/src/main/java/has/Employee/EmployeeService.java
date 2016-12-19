package has.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 12/19/2016.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    public Employee save(Employee employee) {
        return repo.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    public Employee findById(Long id) {
        return repo.findOne(id);
    }

    public Employee remove(Long id) throws Exception {
        Employee employee = repo.findOne(id);
        if (employee == null){
            throw new Exception();
        }
        repo.delete(employee);
        return employee;
    }

    public Employee update(Long id, Employee employee) throws Exception {
        Employee dbEmployee = repo.findOne(id);
        if (dbEmployee == null){
            throw new Exception();
        }

        dbEmployee.setDateHired(employee.getDateHired());
        dbEmployee.setFullName(employee.getFullName());
        dbEmployee.setPost(employee.getPost());

        return repo.save(dbEmployee);
    }
}
