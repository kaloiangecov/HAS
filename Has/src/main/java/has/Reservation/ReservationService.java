package has.Reservation;

import freemarker.template.TemplateException;
import has.Configuration.Settings.HasConfigurationInstance;
import has.ReservationGuest.ReservationGuest;
import has.Room.Room;
import has.Room.RoomRepository;
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

    @Autowired
    private RoomRepository repoRoom;

    private HasConfigurationInstance configurationInstance = HasConfigurationInstance.getInstance();

    private static final int RESERVATION_STATUS_CREATED = 0;
    private static final int RESERVATION_STATUS_ARRIVED = 1;
    private static final int RESERVATION_STATUS_CLOSED = 2;

    public Reservation save(Reservation reservation, User user) throws IOException, TemplateException {
        setLastModified(reservation, user);
        notifyCustomer(reservation);
        return repo.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = repo.findAll();
        return reservations;
    }

    public List<Reservation> searchReservations(String startDate, String endDate, Boolean isGroup) {

        List<Reservation> reservations = new ArrayList<Reservation>();

        if (isGroup != null && isGroup == true) {
            reservations = repo.findGroupReservationsForCalendar(true, startDate, endDate);
        } else {
            reservations = repo.findAllReservationsForCalendar(startDate, endDate);
        }
        return reservations;
    }

    public List<Room> searchReservationsWeb(String startDate, String endDate, int numberAdults, boolean children, boolean pets, boolean minibar) {
        List<Room> freeRooms;

        if (children) {
            freeRooms = repoRoom.findInSiteWithChildren(startDate, endDate, numberAdults, children, pets, minibar);
        } else {
            freeRooms = repoRoom.findInSite(startDate, endDate, numberAdults, pets, minibar);
        }
        return freeRooms;
    }

    public Reservation findById(Long id) throws Exception {
        Reservation reservation = repo.findOne(id);
        validateIdNotNull(reservation);
        return reservation;
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
    }

    public Reservation update(Long id, Reservation reservation, User user) throws Exception {
        Reservation dbReservation = repo.findOne(id);
        validateIdNotNull(dbReservation);

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
        notifyCustomer(dbReservation);
        dbReservation.setReceptionist(reservation.getReceptionist());

        int i = 0;
        for (ReservationGuest dbReservationGuest : dbReservation.getReservationGuests()) {
            dbReservationGuest.setRoom(reservation.getReservationGuests().get(i).getRoom());
            i++;
        }
        return repo.save(dbReservation);
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
        return repo.save(dbReservation);
    }

    public Reservation close(Long id, User user) throws Exception {
        Reservation reservation = repo.findOne(id);
        validateIdNotNull(reservation);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LocalDate startDate = LocalDate.parse(reservation.getStartDate());
        LocalDate endDate = LocalDate.parse(reservation.getEndDate());
        int reservationDuration = Days.daysBetween(startDate, endDate).getDays();

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf2.format(new Date());
        if ((reservation.getStatus() == RESERVATION_STATUS_ARRIVED)) {
            for (ReservationGuest reservationGuest : reservation.getReservationGuests()) {
                int reservationsMade = reservationGuest.getGuest().getNumberReservations();
                reservationGuest.getGuest().setNumberReservations(reservationsMade + reservationDuration);

                if (reservationGuest.getEndDate() == null) {
                    reservationGuest.setEndDate(today);
                }
            }
        }
        //TODO: testing out pricing this is some bullshit here
        {
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

        if (reservation.getEndDate().compareTo(today) > 0)
            reservation.setEndDate(today);

        reservation.setStatus(RESERVATION_STATUS_CLOSED);
        reservation.setLastModifiedBy(user);
        reservation.setLastModifiedTime(sdf.format(new Date()));

        return repo.save(reservation);
    }

    public List<Reservation> getClientHistory(Long id) {
        return repo.findByReservationGuestsGuestId(id);
    }

    private void setLastModified(Reservation reservation, User user) throws IOException, TemplateException {
        reservation.setLastModifiedBy(user);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reservation.setLastModifiedTime(sdf.format(new Date()));
    }

    private void notifyCustomer(Reservation reservation) throws IOException, TemplateException {
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

        if (reservation.getStatus() == RESERVATION_STATUS_ARRIVED) {
            String templatePath = "roomCode.ftl";
            templateHandler.sendMail(model, templatePath, reservationGuest);
        } else if (reservation.getStatus() == RESERVATION_STATUS_CREATED) {
            String templatePath = "register.ftl";
            templateHandler.sendMail(model, templatePath, reservationGuest);
        }
    }

    private void validateIdNotNull(Reservation reservation) throws Exception {
        if (reservation == null) {
            throw new Exception("There is no reservation with such ID");
        }
    }
}
