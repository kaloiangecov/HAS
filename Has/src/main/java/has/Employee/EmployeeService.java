package has.Employee;

import has.Task.TaskRepository;
import has.Utils.TimeFormatter;
import has.Utils.Validator;
import has.WorkingSchedule.WorkingSchedule;
import has.WorkingSchedule.WorkingScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kaloi on 12/19/2016.
 */
@Service
@Transactional()
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private WorkingScheduleRepository workingScheduleRepository;

    @Autowired
    private TimeFormatter timeFormatter;

    public Employee save(Employee employee) throws Exception {
        validateEgn(employee);
        validateIssueDate(employee);
        validateIdentityNumber(employee);

        return repo.save(employee);
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

    public List<Employee> findServiceEmployeesForShift(String date, int shift) {
        return repo.findServiceEmployeesForShift(date, shift);
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

    public List<EmployeeDTO> getEmployeesOnShift(int shift, boolean requiresManager, boolean tomorrowDate) throws Exception {
        String date = timeFormatter.getAsYearMonthDayFormat(new Date());
        if (tomorrowDate) {
            date = timeFormatter.getTomorrowDateAsString();
        }
        List<Employee> employees = null;
        if (!requiresManager) {
            employees = repo.findServiceEmployeesForShift(date, shift);
            if (employees.isEmpty()) {
                throw new Exception("There is no service members on shift right now");
            }
        } else {
            employees = repo.findManagerEmployeesForShift(date, shift);
            if (employees.isEmpty()) {
                throw new Exception("There are no manager members on shift right now");
            }
        }
        List<EmployeeDTO> employeesDTO = new ArrayList<>(employees.size());
        int index = 0;
        for (Employee employee : employees) {
            employeesDTO.add(new EmployeeDTO());
            employeesDTO.get(index).
                    setWorkingSchedule(workingScheduleRepository.findByEmployeeIdAndDate(employee.getId(), date));
            employeesDTO.get(index).setTasks(taskRepo.findByAssigneeIdAndTimePlacedStartingWith(employee.getId(), date));
            index++;
        }
        return employeesDTO;
    }

    public EmployeeDTO transferEmployeeToDTO(Long id, boolean tomorrow) throws Exception {
        String date = timeFormatter.getAsYearMonthDayFormat(new Date());
        if (tomorrow) {
//            date = timeFormatter.getAsYearMonthDayFormat(timeFormatter.getTomorrowDateAsString());
        }
        EmployeeDTO employeeDTO = new EmployeeDTO();
        WorkingSchedule workingSchedule = workingScheduleRepository.findByEmployeeIdAndDate(id, date);
        if (workingSchedule != null) {
            employeeDTO.setWorkingSchedule(workingSchedule);
        } else {
            throw new Exception("Employee with id: " + id + " is not on shift today!");
        }

        employeeDTO.setTasks(taskRepo.findByAssigneeIdAndTimePlacedStartingWith(id, date));
        return employeeDTO;
    }

}
