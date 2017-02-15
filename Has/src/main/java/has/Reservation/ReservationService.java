package has.Reservation;

import freemarker.template.TemplateException;
import has.ReservationGuest.ReservationGuest;
import has.User.User;
import has.Utils.TemplateHandler;
import has.WorkingSchedule.WorkingSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaloi on 12/20/2016.
 */
@Service
public class ReservationService {

    @Autowired
    private TemplateHandler templateHandler;

    @Autowired
    private ReservationRepository repo;

    public static final int RESERVATION_STATUS_ARRIVED = 1;

    public Reservation save(Reservation reservation, User user) throws IOException, TemplateException {
        setLastModifiedAndNotifyCustommer(reservation, user);
        return repo.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = repo.findAll();
        for (Reservation reservation : reservations) {
            reservation = removeRecursions(reservation);
        }
        return reservations;
    }

    public List<Reservation> searchReservations(String startDate, String endDate) {

        List<Reservation> reservations = repo.findByStatusNotAndStartDateGreaterThanAndEndDateLessThan(2, startDate, endDate);

        for (Reservation reservation : reservations) {
            reservation = removeRecursions(reservation);
        }

        return reservations;
    }

    public Reservation findById(Long id) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        if (dbReservation == null) {
            throw new Exception("There is no reservation with such ID");
        }

        return removeRecursions(dbReservation);
    }

    public Reservation remove(Long id) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        if (dbReservation == null) {
            throw new Exception("There is no reservation with such ID");
        }

        repo.delete(dbReservation);
        return removeRecursions(dbReservation);
    }

    public Reservation update(Long id, Reservation reservation, User user) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        if (dbReservation == null) {
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

        setLastModifiedAndNotifyCustommer(dbReservation, user);

        dbReservation.setReceptionist(reservation.getReceptionist());

        int i = 0;
        for (ReservationGuest dbReservationGuest : dbReservation.getReservationGuests()) {
            dbReservationGuest.setRoom(reservation.getReservationGuests().get(i).getRoom());
            i++;
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

    public Reservation close(Long id, User user) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        if (dbReservation == null) {
            throw new Exception("There is no reservation with such ID");
        }

        dbReservation.setStatus(2);
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

    private void setLastModifiedAndNotifyCustommer(Reservation reservation, User user) throws IOException, TemplateException {
        reservation.setLastModifiedBy(user);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reservation.setLastModifiedTime(sdf.format(new Date()));

        if (reservation.getStatus() == RESERVATION_STATUS_ARRIVED) {
            notifyCustommer(reservation, user);
        }
    }

    private void notifyCustommer(Reservation reservation, User user) throws IOException, TemplateException {
        ReservationGuest reservationGuest = null;
        for (ReservationGuest singleReservationGuest : reservation.getReservationGuests()) {
            if (singleReservationGuest.isOwner() == true) {
                reservationGuest = singleReservationGuest;
            }
        }
        Map model = new HashMap();
        model.put("reservation", reservation);
        model.put("guest", reservationGuest.getGuest());
        String templatePath = "roomCode.ftl";

        templateHandler.sendMail(model, templatePath, reservationGuest);
    }
}
