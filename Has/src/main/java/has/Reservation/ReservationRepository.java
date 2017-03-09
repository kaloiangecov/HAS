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

    @Query("SELECT r FROM has.Reservation.Reservation r WHERE r.status <> :status AND ((r.startDate >= :startDate AND r.startDate < :endDate) OR (r.startDate < :startDate AND r.endDate > :startDate AND r.endDate < :endDate))")
    List<Reservation> findAllReservationsForCalendar(@Param("status") int status, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT r FROM has.Reservation.Reservation r WHERE r.group = :group AND r.status <> :status AND (r.startDate >= :startDate AND r.startDate < :endDate) OR (r.startDate < :startDate AND r.endDate > :startDate AND r.endDate < :endDate)")
    List<Reservation> findGroupReservationsForCalendar(@Param("group") Boolean group, @Param("status") int status, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT r FROM has.Room.Room r WHERE r NOT IN (SELECT rg.room FROM has.ReservationGuest.ReservationGuest rg WHERE ((rg.reservation.startDate >= :startDate AND rg.reservation.startDate < :endDate) OR (rg.reservation.startDate < :startDate AND rg.reservation.endDate > :startDate AND rg.reservation.endDate < :endDate)) AND (rg.room.bedsDouble * 2 + rg.room.bedsSingle) > :numberAdults AND rg.reservation.status < 2) AND (r.bedsDouble * 2 + r.bedsSingle) >= :numberAdults")
    List<Room> findInSite(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("numberAdults") int numberAdults);
}
