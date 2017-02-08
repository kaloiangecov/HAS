package has.Employee;

import has.WorkingSchedule.WorkingSchedule;
import has.mailsender.MailTemplates;
import has.mailsender.SendMailSSL;
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
        Employee dbEmployee = repo.findByPersonalDataEgn(employee.getPersonalData().getEgn());
        if (dbEmployee != null)
            throw new Exception("Employee with EGN " + employee.getPersonalData().getEgn() + " already exists.");

        new Thread(
                new Runnable() {
                    public void run() {
                        SendMailSSL.sendMail("shit@hotmail.com", MailTemplates.RESERVATION_CONFIRMATION);
                    }
                }).start();

        return repo.save(employee);
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = repo.findAll();

        for (Employee emp : employees)
            for (WorkingSchedule schedule : emp.getWorkingSchedules())
                schedule.setEmployee(null);

        return employees;
    }

    public Page<Employee> searchEmployees(int start, int length, String fullName, String phone, String dateHired) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.ASC, "id");
        Page<Employee> employeesPage = null;
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

        for (WorkingSchedule schedule : employee.getWorkingSchedules())
            schedule.setEmployee(null);

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

        if (repo.findByPersonalDataEgn(employee.getPersonalData().getEgn()) != null)
            throw new Exception("Employee with EGN " + employee.getPersonalData().getEgn() + " already exists.");

        dbEmployee.setDateHired(employee.getDateHired());
        dbEmployee.setPersonalData(employee.getPersonalData());
        if (dbEmployee.getUser().getId() != employee.getUser().getId()) {
            dbEmployee.setUser(employee.getUser());
        }
        SendMailSSL.sendMail("shit@shit.com", MailTemplates.RESERVATION_CONFIRMATION);
        return repo.save(dbEmployee);
    }
}
