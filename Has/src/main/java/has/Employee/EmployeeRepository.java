package has.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kaloi on 12/19/2016.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByPersonalDataEgn(String egn);

    Page<Employee> findByPersonalDataFullNameContainingAndPersonalDataPhoneContaining(String fullName, String phone, Pageable request);

    Page<Employee> findByPersonalDataFullNameContainingAndPersonalDataPhoneContainingAndDateHired(String fullName, String phone, String dateHired, Pageable pageRequest);
}
