package has.Employee;

import has.Utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 12/19/2016.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    public Employee save(Employee employee) throws Exception {
        validateEgn(employee);
        validateIssueDate(employee);
        validateIdentityNumber(employee);

        return repo.save(employee);
    }

    public List<Employee> getAllEmployedEmployees() {
        List<Employee> employees = repo.findByEmployedTrue();

        return employees;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = repo.findAll();

        return employees;
    }

    public Page<Employee> searchEmployees(int start, int length, String sortColumn, String sortDirection, String fullName, String phone, String dateHired, Boolean showDisabled) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.fromString(sortDirection), sortColumn);

        Page<Employee> employeesPage;

        if (dateHired.isEmpty()) {
            if (showDisabled == null || !showDisabled)
                employeesPage = repo.findByEmployedAndPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContaining(true, fullName, phone, request);
            else
                employeesPage = repo.findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContaining(fullName, phone, request);

        } else {
            if (showDisabled == null || !showDisabled)
                employeesPage = repo.findByEmployedAndPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContainingAndDateHired(true, fullName, phone, dateHired, request);
            else
                employeesPage = repo.findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContainingAndDateHired(fullName, phone, dateHired, request);
        }

        return employeesPage;
    }

    public Employee findById(Long id) throws Exception {
        Employee employee = repo.findOne(id);
        validateIdNotNull(employee);
        return employee;
    }

    public List<Employee> findEmployeesForShift(String date, int shift) {
        return repo.findAllEmployeesForShift(date, shift);
    }

    public Employee findByUserId(Long userId) throws Exception {
        Employee employee = repo.findByUserId(userId);
        return employee;
    }

    public Employee remove(Long id) throws Exception {
        Employee employee = repo.findOne(id);
        validateIdNotNull(employee);
        repo.delete(employee);
        return employee;
    }

    public Employee update(Long id, Employee employee) throws Exception {
        Employee dbEmployee = repo.findOne(id);
        validateIdNotNull(dbEmployee);
        validateIssueDate(employee);
        validateEgn(employee);
        validateIdentityNumber(employee);

        dbEmployee.setDateHired(employee.getDateHired());
        dbEmployee.setPersonalData(employee.getPersonalData());
        dbEmployee.setSalary(employee.getSalary());
        dbEmployee.setBusy(employee.isBusy());
        if (dbEmployee.getUser().getId() != employee.getUser().getId()) {
            dbEmployee.setUser(employee.getUser());
        }
        return repo.save(dbEmployee);
    }

    public Employee changeEmployment(Long id) throws Exception {
        Employee employee = repo.findOne(id);
        validateIdNotNull(employee);
        employee.setEmployed(!employee.isEmployed());
        return repo.save(employee);
    }

    private void validateIssueDate(Employee employee) throws Exception {
        if (!Validator.isValidIssueExpireDate(employee.getPersonalData().getIdentityIssueDate(), employee.getPersonalData().getIdentityExpireDate())) {
            throw new Exception("Invalid issue date");
        }
    }

    private void validateEgn(Employee employee) throws Exception {
        Employee dbEmployee = repo.findByPersonalDataEgn(employee.getPersonalData().getEgn());
        if (dbEmployee != null && dbEmployee.getId() != employee.getId()) {
            throw new Exception("Employee with EGN " + employee.getPersonalData().getEgn() + " already exists.");
        }
    }

    private void validateIdNotNull(Employee employee) throws Exception {
        if (employee == null) {
            throw new Exception("There is no employee with such ID");
        }
    }

    private void validateIdentityNumber(Employee employee) throws Exception {
        Employee dbEmployee = repo.findByPersonalDataIdentityNumber(employee.getPersonalData().getIdentityNumber());
        if (dbEmployee != null && dbEmployee.getId() != employee.getId()) {
            throw new Exception("Employee with Identity Number " + employee.getPersonalData().getIdentityNumber() + " already exists.");
        }
    }
}
