package has.Reservation;

import has.Employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Service
public class ReservationService {

    @Autowired
    private ReservationRepository repo;


    public Reservation save(Reservation reservation, Employee employee) {
        reservation.setReceptionist(employee);
        return repo.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return repo.findAll();
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

    public Reservation update(Long id, Reservation reservation) throws Exception {
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

        return repo.save(dbReservation);
    }
}
