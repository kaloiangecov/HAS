package has.ReservationGuest;

import has.Guest.Guest;
import has.Reservation.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 1/31/2017.
 */
@Service
public class ReservationGuestService {

    @Autowired
    private ReservationGuestRepository repo;

    public ReservationGuest save(ReservationGuest reservationGuest) {
        Guest guest = reservationGuest.getGuest();
        guest.setNumberReservations(guest.getNumberReservations() + 1);

        reservationGuest.setGuest(guest);

        return repo.save(reservationGuest);
    }

    public List<ReservationGuest> getAllReservationGuestConnections() {
        return repo.findAll();
    }

    public ReservationGuest findById(Long id) throws Exception {
        ReservationGuest dbReservationGuest = repo.findOne(id);
        if (dbReservationGuest == null) {
            throw new Exception("There is no ?? with such ID");
        }
        return dbReservationGuest;
    }

    public ReservationGuest findFirstByReservationId(Long id) {
        return repo.findFirstByReservationId(id);
    }

    public ReservationGuest findByReservationIdAndOwner(Long id) {
        return repo.findByReservationIdAndIsOwner(id, true);
    }

    public ReservationGuest remove(Long id) throws Exception {
        ReservationGuest dbReservationGuest = repo.findOne(id);
        if (dbReservationGuest == null) {
            throw new Exception("There is no ?? with such ID");
        }
        repo.delete(dbReservationGuest);
        return dbReservationGuest;
    }

    public ReservationGuest update(Long id, Reservation reservation) throws Exception {
        ReservationGuest dbReservationGuest = repo.findOne(id);
        if (dbReservationGuest == null) {
            throw new Exception("There is no ?? with such ID");
        }

        return repo.save(dbReservationGuest);
    }
}
