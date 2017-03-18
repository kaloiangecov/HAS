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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    public Reservation save(Reservation reservation, boolean isGroup, String groupId, User user) throws IOException, TemplateException {
        setLastModified(reservation, user);

        if (isGroup) {
            if (groupId != null) {
                reservation.setGroupId(groupId);
            } else {
                UUID code = UUID.randomUUID();
                reservation.setGroupId(String.valueOf(code));
            }
        }

        return repo.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = repo.findAll();
        return reservations;
    }

    public List<Reservation> searchReservations(String startDate, String endDate, Boolean isGroup) {

        List<Reservation> reservations = new ArrayList<Reservation>();

        if (isGroup != null && isGroup == true) {
            reservations = repo.findGroupReservationsForCalendar(startDate, endDate);
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
        dbReservation.setDiscount(reservation.getDiscount());

        dbReservation.setEndDate(reservation.getEndDate());
        dbReservation.setNumberAdults(reservation.getNumberAdults());
        dbReservation.setStartDate(reservation.getStartDate());
        dbReservation.setRoom(reservation.getRoom());
        dbReservation.setNumberChildren(reservation.getNumberChildren());

        setLastModified(dbReservation, user);
        templateHandler.notifyCustomer(dbReservation);
        dbReservation.setReceptionist(reservation.getReceptionist());

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

        if ((reservation.getStatus() == RESERVATION_STATUS_ARRIVED)) {
            for (ReservationGuest reservationGuest : reservation.getReservationGuests()) {
                int reservationsMade = reservationGuest.getGuest().getNumberReservations();
                reservationGuest.getGuest().setNumberReservations(reservationsMade + reservationDuration);
            }
        }
        //TODO: testing out pricing this is some bullshit here
        {
            if (reservation.getGroupId() == null) {
                ReservationGuest guest = reservation.getReservationGuests().get(0);
                int bedsSingle = reservation.getRoom().getBedsSingle();
                int bedsDouble = reservation.getRoom().getBedsDouble();
                boolean allInclusive = reservation.isAllInclusive();
                boolean dinner = reservation.isDinner();
                boolean breakfast = reservation.isBreakfast();
                int guestRang = guest.getGuest().getNumberReservations();
                int roomClass = reservation.getRoom().getRoomClass();

                reservation.setPrice(configurationInstance.getReservationCost(bedsSingle, bedsDouble, allInclusive,
                        dinner, breakfast, guestRang, reservationDuration, roomClass));
            }
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf2.format(new Date());
        if (reservation.getEndDate().compareTo(today) > 0)
            reservation.setEndDate(today);

        reservation.setStatus(RESERVATION_STATUS_CLOSED);
        reservation.setLastModifiedBy(user);
        reservation.setLastModifiedTime(sdf.format(new Date()));

        return repo.save(reservation);
    }

    private void setLastModified(Reservation reservation, User user) throws IOException, TemplateException {
        reservation.setLastModifiedBy(user);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reservation.setLastModifiedTime(sdf.format(new Date()));
    }

    private void validateIdNotNull(Reservation reservation) throws Exception {
        if (reservation == null) {
            throw new Exception("There is no reservation with such ID");
        }
    }
}
