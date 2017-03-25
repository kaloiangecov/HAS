package has.ReservationGuest;

import freemarker.template.TemplateException;
import has.Reservation.ReservationRepository;
import has.Utils.TemplateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaloi on 1/31/2017.
 */
@Service
public class ReservationGuestService {

    @Autowired
    private ReservationGuestRepository repo;

    @Autowired
    private ReservationRepository repoReservation;

    @Autowired
    private TemplateHandler templateHandler;

    public ReservationGuest save(ReservationGuest reservationGuest) throws IOException, TemplateException {
        ReservationGuest savedGuest = null;
        savedGuest = repo.save(reservationGuest);
        sendEmailNotification(savedGuest);
        return savedGuest;
    }

    private void sendEmailNotification(ReservationGuest reservationGuest) throws IOException, TemplateException {
        Map model = new HashMap();
        String message = "Thank you for registering in our hotel.";
        model.put("message", message);
        model.put("guest", reservationGuest.getGuest());
        model.put("reservation", reservationGuest.getReservation());
        String templatePath = "register.ftl";

        templateHandler.sendMail(model, templatePath, reservationGuest);
    }

    public List<ReservationGuest> getAllReservationGuestConnections() {
        List<ReservationGuest> reservations = repo.findAll();
        return reservations;
    }

    public ReservationGuest findById(Long id) throws Exception {
        ReservationGuest reservationGuest = repo.findOne(id);
        validateIdNotNull(reservationGuest);
        return reservationGuest;
    }

    public ReservationGuest remove(Long id) throws Exception {
        ReservationGuest reservationGuest = repo.findOne(id);
        validateIdNotNull(reservationGuest);
        repo.delete(reservationGuest);
        return reservationGuest;
    }

    public ReservationGuest update(Long id, ReservationGuest reservationGuest) throws Exception {
        if (repoReservation.findExistingReservationsCount(
                reservationGuest.getReservation().getStartDate(),
                reservationGuest.getReservation().getEndDate(),
                reservationGuest.getReservation().getRoom().getId()) == 0)
            throw new Exception("There already is a reservation on the same room within the same time range! Please, try a new one.");


        ReservationGuest dbReservationGuest = repo.findOne(id);
        validateIdNotNull(dbReservationGuest);

        dbReservationGuest.setReservation(reservationGuest.getReservation());
        dbReservationGuest.setGuest(reservationGuest.getGuest());
        dbReservationGuest.setOwner(reservationGuest.isOwner());

        return repo.save(dbReservationGuest);
    }

    private void validateIdNotNull(ReservationGuest reservationGuest) throws Exception {
        if (reservationGuest == null) {
            throw new Exception("There is no ?? with such ID");
        }
    }
}
