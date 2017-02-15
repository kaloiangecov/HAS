package has.ReservationGuest;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
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
    private TemplateHandler templateHandler;

    @Autowired
    private Configuration configuration;

    public ReservationGuest save(ReservationGuest reservationGuest) throws IOException, TemplateException {
        ReservationGuest savedGuest = null;
        savedGuest = repo.save(reservationGuest);
        sendEmaiNotification(savedGuest);
        return savedGuest;
    }

    private void sendEmaiNotification(ReservationGuest reservationGuest) throws IOException, TemplateException {
        Map model = new HashMap();
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
        ReservationGuest dbReservationGuest = repo.findOne(id);
        if (dbReservationGuest == null) {
            throw new Exception("There is no ?? with such ID");
        }
        return dbReservationGuest;
    }

    public ReservationGuest remove(Long id) throws Exception {
        ReservationGuest dbReservationGuest = repo.findOne(id);
        if (dbReservationGuest == null) {
            throw new Exception("There is no ?? with such ID");
        }
        repo.delete(dbReservationGuest);
        return dbReservationGuest;
    }

    public ReservationGuest update(Long id, ReservationGuest reservationGuest) throws Exception {
        ReservationGuest dbReservationGuest = repo.findOne(id);
        if (dbReservationGuest == null) {
            throw new Exception("There is no ?? with such ID");
        }

        dbReservationGuest.setReservation(reservationGuest.getReservation());
        dbReservationGuest.setGuest(reservationGuest.getGuest());
        dbReservationGuest.setRoom(reservationGuest.getRoom());
        dbReservationGuest.setOwner(reservationGuest.isOwner());

        return repo.save(dbReservationGuest);
    }
}
