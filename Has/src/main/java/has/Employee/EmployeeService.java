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

    public Employee findById(Long id) throws Exception {
        Employee employee = repo.findOne(id);
        if(employee == null){
            throw new Exception("There is no employee with such ID");
        }
        return employee;
    }

    public Employee remove(Long id) throws Exception {
        Employee employee = repo.findOne(id);
        if(employee == null){
            throw new Exception("There is no employee with such ID");
        }
        repo.delete(employee);
        return employee;
    }

    public Employee update(Long id, Employee employee) throws Exception {
        Employee dbEmployee = repo.findOne(id);
        if(dbEmployee == null){
            throw new Exception("There is no employee with such ID");
        }

        dbEmployee.setDateHired(employee.getDateHired());
        dbEmployee.setInternship(employee.getInternship());
//        dbEmployee.setEgn(employee.getEgn());
//        dbEmployee.setAddress(employee.getAddress());
//        dbEmployee.setFullName(employee.getFullName());
//        dbEmployee.setPhone(employee.getPhone());
//        dbEmployee.setIdentityExpireDate(employee.getIdentityExpireDate());
//        dbEmployee.setIdentityIssueDate(employee.getIdentityIssueDate());
//        dbEmployee.setIdentityIssuedBy(employee.getIdentityIssuedBy());
//        dbEmployee.setIdentityNumber(employee.getIdentityNumber());

        return repo.save(dbEmployee);
    }
}
