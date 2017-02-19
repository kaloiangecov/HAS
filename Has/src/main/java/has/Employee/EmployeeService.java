package has.Employee;

import has.Utils.DateUtil;
import has.WorkingSchedule.WorkingSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kaloi on 12/19/2016.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    public Employee save(Employee employee) throws Exception {
        Employee dbEmployee = repo.findByPersonalDataEgn(employee.getPersonalData().getEgn());
        if (dbEmployee != null) {
            throw new Exception("Employee with EGN " + employee.getPersonalData().getEgn() + " already exists.");
        }

        if (!DateUtil.isValid(employee.getPersonalData().getIdentityIssueDate(), employee.getPersonalData().getIdentityExpireDate())){
            throw new Exception("Invalid issue date");
        }

        return repo.save(employee);
    }



    public List<Employee> getAllEmployees() {
        List<Employee> employees = repo.findAll();

        for (Employee emp : employees)
            for (WorkingSchedule schedule : emp.getWorkingSchedules())
                schedule.setEmployee(null);

        return employees;
    }

    public Page<Employee> searchEmployees(int start, int length, String sortColumn, String sortDirection, String fullName, String phone, String dateHired) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.fromString(sortDirection), sortColumn);
        Page<Employee> employeesPage;
        if (dateHired.isEmpty()) {
            employeesPage = repo.findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContaining(fullName, phone, request);
        } else {
            employeesPage = repo.findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContainingAndDateHired(fullName, phone, dateHired, request);
        }

        for (Employee emp : employeesPage)
            for (WorkingSchedule schedule : emp.getWorkingSchedules())
                schedule.setEmployee(null);

        return employeesPage;
    }

    public Employee findById(Long id) throws Exception {
        Employee employee = repo.findOne(id);
        if (employee == null) {
            throw new Exception("There is no employee with such ID");
        }

        for (WorkingSchedule schedule : employee.getWorkingSchedules()) {
            schedule.setEmployee(null);
        }
        return employee;
    }

    public Employee findByUserId(Long userId) throws Exception {
        Employee employee = repo.findByUserId(userId);

        for (WorkingSchedule schedule : employee.getWorkingSchedules()) {
            schedule.setEmployee(null);
        }
        return employee;
    }

    public Employee remove(Long id) throws Exception {
        Employee employee = repo.findOne(id);
        if (employee == null) {
            throw new Exception("There is no employee with such ID");
        }
        repo.delete(employee);
        return employee;
    }

    public Employee update(Long id, Employee employee) throws Exception {
        Employee dbEmployee = repo.findOne(id);
        if (dbEmployee == null) {
            throw new Exception("There is no employee with such ID");
        }
        if (!DateUtil.isValid(employee.getPersonalData().getIdentityIssueDate(), employee.getPersonalData().getIdentityExpireDate())){
            throw new Exception("Invalid issue date");
        }

        Employee dbEmployee2 = repo.findByPersonalDataEgn(employee.getPersonalData().getEgn());
        if (dbEmployee2 != null && dbEmployee2.getId() != employee.getId())
            throw new Exception("Employee with EGN " + employee.getPersonalData().getEgn() + " already exists.");

        dbEmployee.setDateHired(employee.getDateHired());
        dbEmployee.setPersonalData(employee.getPersonalData());
        if (dbEmployee.getUser().getId() != employee.getUser().getId()) {
            dbEmployee.setUser(employee.getUser());
        }
//        SendMailSSL.sendMail("shit@shit.com", "");
        return repo.save(dbEmployee);
    }
}
