package has.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kaloi on 12/19/2016.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM has.Employee.Employee e, has.WorkingSchedule.WorkingSchedule ws WHERE ws.date = :date AND ws.shift = :shift AND e = ws.employee")
    List<Employee> findAllEmployeesForShift(@Param(value = "date") String date, @Param(value = "shift") int shift);

    @Query("SELECT e FROM has.Employee.Employee e, has.WorkingSchedule.WorkingSchedule ws WHERE e.user.userRole.id = 4 AND e.employed = true AND ws.date = :date AND ws.shift = :shift AND e = ws.employee")
    List<Employee> findServiceEmployeesForShift(@Param(value = "date") String date, @Param(value = "shift") int shift);

    @Query("SELECT e FROM has.Employee.Employee e, has.WorkingSchedule.WorkingSchedule ws WHERE e.user.userRole.id = 2 AND e.employed = true AND ws.date = :date AND ws.shift = :shift AND e = ws.employee")
    List<Employee> findManagerEmployeesForShift(@Param(value = "date") String date, @Param(value = "shift") int shift);


    @Query("SELECT e FROM has.Employee.Employee e, has.WorkingSchedule.WorkingSchedule ws WHERE ws.date = :date AND ws.shift = :shift AND e = ws.employee AND e.busy is false AND e.employed is true")
    List<Employee> findAllAvailableEmployeesForShift(@Param("date") String date, @Param("shift") int shift);

    List<Employee> findByBusyFalse();

    List<Employee> findByEmployedTrue();

    Employee findByPersonalDataEgn(String egn);

    Employee findByPersonalDataIdentityNumber(String identityNumber);

    Employee findByUserId(Long id);

    Page<Employee> findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContaining(String fullName, String phone, Pageable pageRequest);

    Page<Employee> findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContainingAndDateHired(String fullName, String phone, String dateHired, Pageable pageRequest);

    Page<Employee> findByEmployedAndPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContaining(Boolean employed, String fullName, String phone, Pageable request);

    Page<Employee> findByEmployedAndPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContainingAndDateHired(Boolean employed, String fullName, String phone, String dateHired, Pageable pageRequest);
}
