package has.Reservation;

import has.Room.Room;
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

    List<Reservation> findByReservationGuestsGuestId(Long id);

    Reservation findByReservationCodeAndStatus(String reservationCode, int status);

    @Query("SELECT r FROM has.Reservation.Reservation r WHERE ((r.startDate BETWEEN :startDate AND :endDate) OR (r.endDate BETWEEN :startDate AND :endDate))")
    List<Reservation> findAllReservationsForCalendar(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT r FROM has.Reservation.Reservation r WHERE r.group = :group AND ((r.startDate BETWEEN :startDate AND :endDate) OR (r.endDate BETWEEN :startDate AND :endDate))")
    List<Reservation> findGroupReservationsForCalendar(@Param("group") Boolean group, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT r FROM has.Room.Room r WHERE (r.bedsSingle + r.bedsDouble*2) >= :numberAdults AND r NOT IN (SELECT rg.room FROM has.ReservationGuest.ReservationGuest rg WHERE (rg.reservation.startDate BETWEEN :startDate AND :endDate) OR (rg.reservation.endDate BETWEEN :startDate AND :endDate))")
    List<Room> findInSite(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("numberAdults") int numberAdults);
}
