package has.Employee;

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

    @Query("select e from Employee e where e.personalData.fullName like %:fullName%")
    List<Employee> findByFullName(@Param("fullName") String fullName);

    @Query("select e from Employee e where e.personalData.fullName like %:fullName% and e.personalData.phone like %:phone%")
    List<Employee> findByFullNameAndPhone(@Param("fullName") String fullName, @Param("phone") String phone);

    @Query("select e from Employee e where e.personalData.fullName like %:fullName% and e.personalData.phone like %:phone% and e.dateHired = :dateHired")
    List<Employee> findByFullNameAndPhoneAndDateHired(
            @Param("fullName") String fullName,
            @Param("phone") String phone,
            @Param("dateHired") String dateHired);
}
