package has.Reservation;

import has.ReservationGuest.ReservationGuest;
import has.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Service
public class ReservationService {

    @Autowired
    private ReservationRepository repo;


    public Reservation save(Reservation reservation, User user) {
        reservation.setLastModifiedBy(user);
        return repo.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = repo.findAll();

        for (Reservation reservation : reservations) {
            for (ReservationGuest reservationGuest : reservation.getReservationGuests()) {
                reservationGuest.setReservation(null);
            }
        }

        return reservations;
    }

    public Reservation findById(Long id) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        if(dbReservation == null){
            throw new Exception("There is no reservation with such ID");
        }
        return dbReservation;
    }

    public Reservation remove(Long id) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        if(dbReservation == null){
            throw new Exception("There is no reservation with such ID");
        }
        repo.delete(dbReservation);
        return dbReservation;
    }

    public Reservation update(Long id, Reservation reservation, User user) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        if(dbReservation == null){
            throw new Exception("There is no reservation with such ID");
        }

        dbReservation.setStatus(reservation.getStatus());
        dbReservation.setPrice(reservation.getPrice());
        dbReservation.setAllInclusive(reservation.isAllInclusive());
        dbReservation.setBreakfast(reservation.isBreakfast());
        dbReservation.setDinner(reservation.isDinner());
        dbReservation.setDiscount(reservation.getDiscount());

        dbReservation.setEndDate(reservation.getEndDate());
        dbReservation.setGroup(reservation.isGroup());
        dbReservation.setNumberAdults(reservation.getNumberAdults());
        dbReservation.setStartDate(reservation.getStartDate());
        dbReservation.setNumberChildren(reservation.getNumberChildren());
        dbReservation.setLastModifiedBy(user);
        dbReservation.setLastModifiedTime((new Date().toString()));
        dbReservation.setReceptionist(reservation.getReceptionist());

        return repo.save(dbReservation);
    }
}
