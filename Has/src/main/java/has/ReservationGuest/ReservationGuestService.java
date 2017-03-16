package has.ReservationGuest;

import freemarker.template.TemplateException;
import has.Utils.TemplateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        ReservationGuest dbReservationGuest = repo.findOne(id);
        validateIdNotNull(dbReservationGuest);

        dbReservationGuest.setReservation(reservationGuest.getReservation());
        dbReservationGuest.setGuest(reservationGuest.getGuest());
        dbReservationGuest.setRoom(reservationGuest.getRoom());
        dbReservationGuest.setOwner(reservationGuest.isOwner());

        return repo.save(dbReservationGuest);
    }

    public List<ReservationGuest> closeGroupReservation(Long reservationId, Long roomId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ReservationGuest> guests = repo.findByReservationIdAndRoomId(reservationId, roomId);
        for (ReservationGuest guest : guests) {
            guest.setEndDate(sdf.format(new Date()));
        }
        //TODO: price calculation goes here
        return repo.save(guests);
    }

    private void validateIdNotNull(ReservationGuest reservationGuest) throws Exception {
        if (reservationGuest == null) {
            throw new Exception("There is no ?? with such ID");
        }
    }
}
