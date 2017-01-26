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

    List<Employee> findByPersonalDataFullNameLike(String fullName);

    List<Employee> findByPersonalDataFullNameLikeAndPersonalDataPhoneLike(String fullName, String phone);

    Page<Employee> findByPersonalDataFullNameLikeAndPersonalDataPhoneLike(String fullName, String phone, Pageable pageRequest);

    List<Employee> findByPersonalDataFullNameLikeAndPersonalDataPhoneLikeAndDateHired(String fullName, String phone, String dateHired);

    Page<Employee> findByPersonalDataFullNameLikeAndPersonalDataPhoneLikeAndDateHired(String fullName, String phone, String dateHired, Pageable pageRequest);


    //    @Query("select e from Employee e where e.personalData.fullName like %:fullName%")
//    List<Employee> findByFullName(@Param("fullName") String fullName);

    //    @Query("select e from Employee e where e.personalData.fullName like %:fullName% and e.personalData.phone like %:phone%")
//    List<Employee> findByFullNameAndPhone(@Param("fullName") String fullName, @Param("phone") String phone);

//    @Query("select e from Employee e where e.personalData.fullName like %:fullName% and e.personalData.phone like %:phone%")
//    Page<Employee> findByFullNameAndPhone(@Param("fullName") String fullName, @Param("phone") String phone, Pageable pageRequest);

//    @Query("select e from Employee e where e.personalData.fullName like %:fullName% and e.personalData.phone like %:phone% and e.dateHired = :dateHired")
//    List<Employee> findByFullNameAndPhoneAndDateHired(
//            @Param("fullName") String fullName,
//            @Param("phone") String phone,
//            @Param("dateHired") String dateHired);

    //    @Query("select e from Employee e where e.personalData.fullName like %:fullName% and e.personalData.phone like %:phone% and e.dateHired = :dateHired")
//    Page<Employee> findByFullNameAndPhoneAndDateHired(
//            @Param("fullName") String fullName,
//            @Param("phone") String phone,
//            @Param("dateHired") String dateHired,
//            Pageable pageRequest);
}
