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

    List<Employee> findByPersonalDataFullNameLike(String fullName);

    List<Employee> findByPersonalDataFullNameLikeAndPersonalDataPhoneLike(String fullName, String phone);

    Page<Employee> findByPersonalDataFullNameContainingAndPersonalDataPhoneContaining(String fullName, String phone, Pageable pageRequest);

    List<Employee> findByPersonalDataFullNameLikeAndPersonalDataPhoneLikeAndDateHired(String fullName, String phone, String dateHired);

    Page<Employee> findByPersonalDataFullNameContainingAndPersonalDataPhoneContainingAndDateHired(String fullName, String phone, String dateHired, Pageable pageRequest);
}
