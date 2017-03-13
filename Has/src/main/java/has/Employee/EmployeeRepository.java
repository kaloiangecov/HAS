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
