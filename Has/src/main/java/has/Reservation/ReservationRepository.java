package has.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatusNotAndStartDateGreaterThanAndEndDateLessThan(int status, String startDate, String endDate);

    List<Reservation> findByGroupAndStatusNotAndStartDateGreaterThanAndEndDateLessThan(Boolean group, int status, String startDate, String endDate);

    @Query("SELECT r FROM has.Reservation.Reservation r WHERE r.status <> :status AND ((r.startDate >= :startDate AND r.startDate < :endDate) OR (r.startDate < :startDate AND r.endDate > :startDate AND r.endDate < :endDate))")
    List<Reservation> findAllReservationsForCalendar(@Param("status") int status, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT r FROM has.Reservation.Reservation r WHERE r.group = :group AND r.status <> :status AND (r.startDate >= :startDate AND r.startDate < :endDate) OR (r.startDate < :startDate AND r.endDate > :startDate AND r.endDate < :endDate)")
    List<Reservation> findGroupReservationsForCalendar(@Param("group") Boolean group, @Param("status") int status, @Param("startDate") String startDate, @Param("endDate") String endDate);


}
