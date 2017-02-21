package has.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kaloi on 12/19/2016.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByEmployedTrue();

    Employee findByPersonalDataEgn(String egn);

    Employee findByUserId(Long id);

    Page<Employee> findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContainingAndEmployed(String fullName, String phone, Pageable request, boolean employed);

    Page<Employee> findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContainingAndDateHiredAndEmployed(String fullName, String phone, String dateHired, Pageable pageRequest, boolean employed);
}
