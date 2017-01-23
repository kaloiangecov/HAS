package has.Guest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query("select g from Guest g where g.personalData.fullName like %:fullName%")
    List<Guest> findByFullName(@Param("fullName") String fullName);

    @Query("select e from Guest e where e.personalData.fullName like %:fullName% and e.personalData.phone like %:phone%")
    List<Guest> findByFullNameAndPhone(@Param("fullName") String fullName, @Param("phone") String phone);

    @Query("select e from Guest e where e.personalData.fullName like %:fullName% and e.personalData.phone like %:phone%")
    Page<Guest> findByFullNameAndPhone(@Param("fullName") String fullName, @Param("phone") String phone, Pageable pageRequest);
}
