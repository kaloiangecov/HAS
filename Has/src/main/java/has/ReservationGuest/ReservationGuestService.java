package has.ReservationGuest;

import freemarker.template.TemplateException;
import has.Utils.TemplateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public ReservationGuest save(ReservationGuest reservationGuest) throws IOException, TemplateException {
        ReservationGuest savedGuest = null;
        savedGuest = repo.save(reservationGuest);
        sendEmaiNotification(savedGuest);
//        templateHandler.notifyCustomer(savedGuest.getReservation());
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
        dbReservationGuest.setOwner(reservationGuest.isOwner());

        return repo.save(dbReservationGuest);
    }

    public Page<ReservationGuest> getClientHistory(Long id, int start, int length, String sortColumn, String sortDirection) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.fromString(sortDirection), sortColumn);
        return repo.findByGuestId(id, request);
    }

    private void validateIdNotNull(ReservationGuest reservationGuest) throws Exception {
        if (reservationGuest == null) {
            throw new Exception("There is no ?? with such ID");
        }
    }
}
