package has.Guest;

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
}
