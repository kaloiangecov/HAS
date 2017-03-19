package has.Guest;

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
public interface GuestRepository extends JpaRepository<Guest, Long> {

    Guest findByPersonalDataEgn(String egn);

    Guest findByPersonalDataIdentityNumber(String identityNumber);

    Guest findByUserEmail(String email);

    Page<Guest> findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContaining(String fullName, String phone, Pageable pageRequest);

    @Query("select g from has.Guest.Guest g where g not in (select rg.guest from has.ReservationGuest.ReservationGuest rg where rg.reservation.id = :reservationId)")
    List<Guest> findReservationFreeGuests(@Param("reservationId") Long reservationId);

    @Query("select g from has.Guest.Guest g where g not in (select rg.guest from has.ReservationGuest.ReservationGuest rg where rg.reservation.groupId = :groupId)")
    List<Guest> findGroupReservationFreeGuests(@Param("groupId") String groupId);
}
