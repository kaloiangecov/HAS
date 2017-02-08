package has.Employee;

import has.Utils.QueryToPageExecutor;
import has.mailsender.MailTemplates;
import has.mailsender.SendMailSSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by kaloi on 12/19/2016.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    private EntityManager entityManager;

    private QueryToPageExecutor queryToPageExecutor;

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
        return repo.findAll();
    }

    public Page<Employee> searchEmployees(int start, int length, String fullName, String phone, String dateHired) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.ASC, "id");
//        Long page = Long.valueOf(start);
//        has.Utils.PageRequest request = new has.Utils.PageRequest(page, length);
//
//        QEmployee employee = QEmployee.employee;
//        JPQLQuery query = new JPAQuery(entityManager);
//
//        Page<Employee> employees = null;
//        query.from(employee)
//                .where(employee.personalData.fullName.containsIgnoreCase(fullName))
//                .where(employee.personalData.phone.contains(phone));
//        if (!dateHired.isEmpty()) {
//            query.from(employee)
//                    .where(employee.dateHired.eq(dateHired));
//        }
//
//        return queryToPageExecutor.getPage(query, request);


        if (dateHired.isEmpty()) {
            return repo.findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContaining(fullName, phone, request);
        } else {
            return repo.findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContainingAndDateHired(fullName, phone, dateHired, request);
        }
    }

    public Employee findById(Long id) throws Exception {
        Employee employee = repo.findOne(id);
        if (employee == null) {
            throw new Exception("There is no employee with such ID");
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

        dbEmployee.setDateHired(employee.getDateHired());
        dbEmployee.setPersonalData(employee.getPersonalData());
        if (dbEmployee.getUser().getId() != employee.getUser().getId()) {
            dbEmployee.setUser(employee.getUser());
        }
        SendMailSSL.sendMail("shit@shit.com", MailTemplates.RESERVATION_CONFIRMATION);
        return repo.save(dbEmployee);
    }
}
