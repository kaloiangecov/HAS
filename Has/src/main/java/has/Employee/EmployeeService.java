package has.Employee;

import has.mailsender.MailTemplates;
import has.mailsender.SendMailSSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

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

    public Page<Employee> searchEmployees(int draw, int start, int length, String fullName, String phone, String dateHired) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.ASC, "id");

        if (dateHired.isEmpty())
            return repo.findByFullNameAndPhone(fullName, phone, request);
        else
            return repo.findByFullNameAndPhoneAndDateHired(fullName, phone, dateHired, request);
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
        dbEmployee.setPersonalData(employee.getPersonalData());
        if (dbEmployee.getUser().getId() != employee.getUser().getId())
            dbEmployee.setUser(employee.getUser());

        SendMailSSL.sendMail("shit@shit.com", MailTemplates.RESERVATION_CONFIRMATION);
        return repo.save(dbEmployee);
    }
}
