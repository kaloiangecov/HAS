package has.Employee;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gundev on 17.1.2017 г..
 */
public class EmployeeServiceTest {

    @InjectMocks
    EmployeeService employeeService;
    @Mock
    EmployeeRepository employeeRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = new LinkedList<>();
        Employee emp1 = new Employee();
        Employee emp2 = new Employee();
        Employee emp3 = new Employee();
        Employee emp4 = new Employee();

        employees.add(emp1);
        employees.add(emp2);
        employees.add(emp3);
        employees.add(emp4);
        Mockito.when(employeeRepository.findAll()).thenReturn(employees);
        assertEquals(employeeService.getAllEmployees(), employees);
        assertNotNull(employeeService.getAllEmployees());
    }

    @Test(expected = Exception.class)
    public void testFindByIdFail() throws Exception {
        employeeService.findById(null);
    }

    @Test
    public void testFindById() throws Exception {
        Employee emp1 = new Employee();
        Mockito.when(employeeRepository.findOne(23456L)).thenReturn(emp1);
        assertNotNull(employeeService.findById(23456L));
    }

    @Test(expected = Exception.class)
    public void testUpdateFail() throws Exception {
        employeeService.findById(null);
    }

    @Test
    public void testUpdate() throws Exception {
        Employee emp1 = new Employee();
        Employee emp2 = new Employee("today", 2);
        Mockito.when(employeeRepository.findOne(23456L)).thenReturn(emp1);
        Mockito.when(employeeRepository.save(emp1)).thenReturn(emp2);
        assertEquals(employeeService.update(23456L, emp2), emp2);
    }
}