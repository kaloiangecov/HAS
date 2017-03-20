package has.Reservation;

import freemarker.template.TemplateException;
import has.ReservationGuest.ReservationGuest;
import has.Room.Room;
import has.Room.RoomRepository;
import has.User.User;
import has.Utils.CalculationUtils;
import has.Utils.TemplateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        //reservation.setPrice(CalculationUtils.getReservationCost(reservation));
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
        //dbReservation.setPrice(CalculationUtils.getReservationCost(dbReservation));
        return repo.save(dbReservation);
    }

    public List<Reservation> updateGroupReservationDetails(Reservation reservation, String roomIds) throws Exception {
        List<Reservation> groups = repo.findByGroupId(reservation.getGroupId());
        if (groups == null || groups.size() == 0)
            throw new Exception("There aren't any reservations with such group ID!");

        String[] arrRoomIds = null;
        int i = 0;
        boolean editRooms = roomIds == "-";

        if (editRooms)
            arrRoomIds = roomIds.split(",");

        for (Reservation gr : groups) {
            gr.setPrice(reservation.getPrice());
            gr.setAllInclusive(reservation.isAllInclusive());
            gr.setBreakfast(reservation.isBreakfast());
            gr.setDinner(reservation.isDinner());

            gr.setStartDate(reservation.getStartDate());
            gr.setEndDate(reservation.getEndDate());
            gr.setNumberAdults(reservation.getNumberAdults());
            gr.setNumberChildren(reservation.getNumberChildren());

            if (editRooms) {
                gr.setRoom(repoRoom.getOne(Long.parseLong(arrRoomIds[i])));
                i++;
            }

            setLastModified(gr, reservation.getReservationGuests().get(0).getGuest().getUser());
            templateHandler.notifyCustomer(gr);
        }

        return repo.save(groups);
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

    public Reservation checkIn(Long id, User user) throws Exception {
        Reservation reservation = repo.findOne(id);
        validateIdNotNull(reservation);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reservation.setStatus(RESERVATION_STATUS_ARRIVED);
        reservation.getRoom().setStatus(1);
        reservation.setLastModifiedBy(user);
        reservation.setLastModifiedTime(sdf.format(new Date()));

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf2.format(new Date());
        if (reservation.getGroupId() != null) {
            List<Reservation> groupReservations = repo.findByGroupId(reservation.getGroupId());
            for (Reservation gr : groupReservations) {
                if (gr.getId() != reservation.getId()) {
                    gr.setStatus(RESERVATION_STATUS_ARRIVED);
                    gr.getRoom().setStatus(1);
                    gr.setLastModifiedBy(user);
                    gr.setLastModifiedTime(sdf.format(new Date()));

                    repo.save(gr);
                }
            }
        }

        return repo.save(reservation);
    }

    public Reservation close(Long id, User user) throws Exception {
        Reservation reservation = repo.findOne(id);
        validateIdNotNull(reservation);

        if ((reservation.getStatus() == RESERVATION_STATUS_ARRIVED)) {
            for (ReservationGuest reservationGuest : reservation.getReservationGuests()) {
                int reservationsMade = reservationGuest.getGuest().getNumberReservations();
                reservationGuest.getGuest().setNumberReservations(reservationsMade +
                        CalculationUtils.getReservationDuration(reservation));
            }
        }
        //TODO: testing out pricing this is some bullshit here
        {
//            if (reservation.getGroupId() == null) {
//                reservation.setPrice(CalculationUtils.getReservationCost(reservation));
//            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf2.format(new Date());
        if (reservation.getEndDate().compareTo(today) > 0)
            reservation.setEndDate(today);

        reservation.setStatus(RESERVATION_STATUS_CLOSED);
        reservation.getRoom().setStatus(2);
        reservation.setLastModifiedBy(user);
        reservation.setLastModifiedTime(sdf.format(new Date()));

        if (reservation.getGroupId() != null && reservation.getReservationGuests().get(0).isOwner()) {
            List<Reservation> groupReservations = repo.findByGroupId(reservation.getGroupId());
            for (Reservation gr : groupReservations) {
                if (gr.getId() != reservation.getId() && gr.getStatus() < 2) {
                    if (gr.getEndDate().compareTo(today) > 0)
                        gr.setEndDate(today);

                    gr.setStatus(RESERVATION_STATUS_CLOSED);
                    gr.getRoom().setStatus(2);
                    gr.setLastModifiedBy(user);
                    gr.setLastModifiedTime(sdf.format(new Date()));

                    repo.save(gr);
                }
            }
        }

        return repo.save(reservation);
    }

    public Page<Reservation> getClientHistory(Long id, int start, int length, String sortColumn, String sortDirection) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.fromString(sortDirection), sortColumn);
        return repo.findByGuestId(id, request);
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
