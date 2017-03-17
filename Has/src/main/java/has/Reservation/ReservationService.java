package has.Reservation;

import freemarker.template.TemplateException;
import has.Configuration.Settings.HasConfigurationInstance;
import has.Configuration.Settings.HasConfigurationRepository;
import has.ReservationGuest.ReservationGuest;
import has.Room.Room;
import has.User.User;
import has.Utils.TemplateHandler;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public static final int RESERVATION_STATUS_CLOSED = 2;

    @Autowired
    private HasConfigurationRepository repoConfig;

    private HasConfigurationInstance configurationInstance = HasConfigurationInstance.getInstance();


    public Reservation save(Reservation reservation, User user) throws IOException, TemplateException {
        setLastModified(reservation, user);
        notifyCustomer(reservation, user);
        return repo.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = repo.findAll();
//        for (Reservation reservation : reservations) {
//            reservation = removeRecursions(reservation);
//        }
        return reservations;
    }

    public List<Reservation> searchReservations(String startDate, String endDate, Boolean isGroup) {

        List<Reservation> reservations = new ArrayList<Reservation>();

        if (isGroup != null && isGroup == true) {
            reservations = repo.findGroupReservationsForCalendar(true, startDate, endDate);
        } else {
            reservations = repo.findAllReservationsForCalendar(startDate, endDate);
        }

        for (Reservation reservation : reservations) {
            reservation = removeRecursions(reservation);
        }

        return reservations;
    }

    public List<Room> searchReservationsWeb(String startDate, String endDate, int numberAdults, boolean children, boolean pets, boolean minibar) {
        List<Room> freeRooms;

        if (children)
            freeRooms = repo.findInSiteWithChildren(startDate, endDate, numberAdults, children, pets, minibar);
        else
            freeRooms = repo.findInSite(startDate, endDate, numberAdults, pets, minibar);

        return freeRooms;
    }

    public Reservation findById(Long id) throws Exception {
        Reservation reservation = repo.findOne(id);
        validateIdNotNull(reservation);

        return reservation;
//        return removeRecursions(reservation);
    }

    public Reservation findByCode(String code) throws Exception {
        Reservation reservation = repo.findByReservationCodeAndStatus(code, 0);

        if (reservation == null)
            throw new Exception("There's no reservation with such code!");

        return reservation;
    }

    public Reservation remove(Long id) throws Exception {
        Reservation reservation = repo.findOne(id);
        validateIdNotNull(reservation);

        repo.delete(reservation);
        return reservation;
//        return removeRecursions(reservation);
    }

    public Reservation update(Long id, Reservation reservation, User user) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        validateIdNotNull(dbReservation);

        //TODO: closed does not work too well should remove it and use this one instead
//        if (reservation.getStatus() == RESERVATION_STATUS_CLOSED) {
//            for (ReservationGuest reservationGuest : dbReservation.getReservationGuests()) {
//                int reservationsMade = reservationGuest.getGuest().getNumberReservations();
//                reservationGuest.getGuest().setNumberReservations(reservationsMade + 1);
//            }
//        }

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

        setLastModified(dbReservation, user);
        notifyCustomer(dbReservation, user);

        dbReservation.setReceptionist(reservation.getReceptionist());

        int i = 0;
        for (ReservationGuest dbReservationGuest : dbReservation.getReservationGuests()) {
            dbReservationGuest.setRoom(reservation.getReservationGuests().get(i).getRoom());
            i++;
        }

        return repo.save(dbReservation);
//        return removeRecursions(repo.save(dbReservation));
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
        Reservation reservation = repo.findOne(id);
        validateIdNotNull(reservation);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LocalDate startDate = LocalDate.parse(reservation.getStartDate());
        LocalDate endDate = LocalDate.parse(reservation.getEndDate());
        int reservationDuration = Days.daysBetween(startDate, endDate).getDays();

        if ((reservation.getStatus() == RESERVATION_STATUS_ARRIVED)) {
            for (ReservationGuest reservationGuest : reservation.getReservationGuests()) {
                int reservationsMade = reservationGuest.getGuest().getNumberReservations();
                reservationGuest.getGuest().setNumberReservations(reservationsMade + reservationDuration);
            }
        }
        //TODO: testing out pricing this is some bullshit here
        {
//            HasConfiguration hasConfiguration = repoConfig.findOne((long) 1);
            if (reservation.isGroup() == false) {
                ReservationGuest guest = reservation.getReservationGuests().get(0);
                int bedsSingle = guest.getRoom().getBedsSingle();
                int bedsDouble = guest.getRoom().getBedsDouble();
                boolean allInclusive = reservation.isAllInclusive();
                boolean dinner = reservation.isDinner();
                boolean breakfast = reservation.isBreakfast();
                int guestRang = guest.getGuest().getNumberReservations();
                int roomClass = guest.getRoom().getRoomClass();

                reservation.setPrice(configurationInstance.getReservationCost(bedsSingle, bedsDouble, allInclusive,
                        dinner, breakfast, guestRang, reservationDuration, roomClass));
            }
        }

        reservation.setStatus(RESERVATION_STATUS_CLOSED);
        reservation.setLastModifiedBy(user);
        reservation.setLastModifiedTime(sdf.format(new Date()));

        return repo.save(reservation);
//        return removeRecursions(repo.save(reservation));
    }

    public List<Reservation> getClientHistory(Long id) {
        return repo.findByReservationGuestsGuestId(id);
    }

    private Reservation removeRecursions(Reservation reservation) {
        for (ReservationGuest reservationGuest : reservation.getReservationGuests()) {
            reservationGuest.setReservation(null);
        }

//        List<WorkingSchedule> schedules = reservation.getReceptionist().getWorkingSchedules();
//        for (WorkingSchedule schedule : schedules) {
//            schedule.setEmployee(null);
//        }
//        reservation.getReceptionist().setWorkingSchedules(schedules);

        return reservation;
    }

    private void setLastModified(Reservation reservation, User user) throws IOException, TemplateException {
        reservation.setLastModifiedBy(user);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reservation.setLastModifiedTime(sdf.format(new Date()));
    }

    private void notifyCustomer(Reservation reservation, User user) throws IOException, TemplateException {
        if (reservation.getStatus() == RESERVATION_STATUS_ARRIVED) {
            ReservationGuest reservationGuest = null;
            for (ReservationGuest singleReservationGuest : reservation.getReservationGuests()) {
                if (singleReservationGuest.isOwner()) {
                    reservationGuest = singleReservationGuest;
                    break;
                }
            }
            Map model = new HashMap();
            model.put("reservation", reservation);
            model.put("guest", reservationGuest.getGuest());
            String templatePath = "roomCode.ftl";

            templateHandler.sendMail(model, templatePath, reservationGuest);
        }
    }

    private void validateIdNotNull(Reservation reservation) throws Exception {
        if (reservation == null) {
            throw new Exception("There is no reservation with such ID");
        }
    }
}
