package has.Reservation;

import has.ReservationGuest.ReservationGuest;
import has.Room.Room;
import has.User.User;
import has.WorkingSchedule.WorkingSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reservation.setLastModifiedTime(sdf.format(new Date()));
        return repo.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = repo.findAll();

        for (Reservation reservation : reservations) {
            reservation = removeRecursions(reservation);
        }
        return reservations;
    }

    public Reservation findById(Long id) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        if(dbReservation == null){
            throw new Exception("There is no reservation with such ID");
        }

        return removeRecursions(dbReservation);
    }

    public Reservation remove(Long id) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        if(dbReservation == null){
            throw new Exception("There is no reservation with such ID");
        }

        repo.delete(dbReservation);

        return removeRecursions(dbReservation);
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
        dbReservation.setGroup(reservation.isGroup());
        dbReservation.setDiscount(reservation.getDiscount());

        dbReservation.setEndDate(reservation.getEndDate());
        dbReservation.setGroup(reservation.isGroup());
        dbReservation.setNumberAdults(reservation.getNumberAdults());
        dbReservation.setStartDate(reservation.getStartDate());
        dbReservation.setNumberChildren(reservation.getNumberChildren());
        dbReservation.setLastModifiedBy(user);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dbReservation.setLastModifiedTime(sdf.format(new Date()));

        dbReservation.setReceptionist(reservation.getReceptionist());

        Room newRoom = reservation.getReservationGuests().get(0).getRoom();
        for (ReservationGuest reservationGuest : dbReservation.getReservationGuests()) {
            reservationGuest.setRoom(newRoom);
        }

        return removeRecursions(repo.save(dbReservation));
    }

    public Reservation move(Long id, Reservation reservation, User user) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        if (dbReservation == null) {
            throw new Exception("There is no reservation with such ID");
        }

        dbReservation.setStartDate(reservation.getStartDate());
        dbReservation.setEndDate(reservation.getEndDate());
        dbReservation.setLastModifiedBy(user);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dbReservation.setLastModifiedTime(sdf.format(new Date()));

        return removeRecursions(repo.save(dbReservation));
    }

    private Reservation removeRecursions(Reservation reservation) {
        for (ReservationGuest reservationGuest : reservation.getReservationGuests()) {
            reservationGuest.setReservation(null);
        }

        List<WorkingSchedule> schedules = reservation.getReceptionist().getWorkingSchedules();
        for (WorkingSchedule schedule : schedules) {
            schedule.setEmployee(null);
        }
        reservation.getReceptionist().setWorkingSchedules(schedules);

        return reservation;
    }
}
