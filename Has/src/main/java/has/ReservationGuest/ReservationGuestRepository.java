package has.ReservationGuest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kaloi on 1/31/2017.
 */
@Repository
public interface ReservationGuestRepository extends JpaRepository<ReservationGuest, Long> {

    List<ReservationGuest> findByReservationIdAndRoomId(Long reservationId, Long roomId);
}
