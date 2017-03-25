package has.Reservation;

import org.springframework.data.domain.Page;
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
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findByReservationCodeAndStatus(String reservationCode, int status);

    List<Reservation> findByGroupId(String groupId);

    @Query("select r from has.Reservation.Reservation r where r in (select rg.reservation from has.ReservationGuest.ReservationGuest rg where rg.guest.id = :id)")
    Page<Reservation> findByGuestId(@Param("id") Long id, Pageable request);

    @Query("SELECT r FROM has.Reservation.Reservation r WHERE ((r.startDate BETWEEN :startDate AND :endDate) OR (r.endDate BETWEEN :startDate AND :endDate))")
    List<Reservation> findAllReservationsForCalendar(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT r FROM has.Reservation.Reservation r WHERE r.groupId is not null AND ((r.startDate BETWEEN :startDate AND :endDate) OR (r.endDate BETWEEN :startDate AND :endDate))")
    List<Reservation> findGroupReservationsForCalendar(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT r FROM has.Reservation.Reservation r WHERE r.id <> :reservationId AND r.room.id = :roomId AND ((r.startDate >= :startDate AND r.startDate < :endDate) OR (r.endDate > :startDate AND r.endDate <= :endDate))")
    Reservation findExistingReservationInSlot(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("roomId") Long roomId, @Param("reservationId") Long reservationId);

}
