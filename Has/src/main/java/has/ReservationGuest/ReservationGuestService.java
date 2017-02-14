package has.ReservationGuest;

import freemarker.template.*;
import has.mailsender.SendMailSSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
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
    private Configuration configuration;

    public ReservationGuest save(ReservationGuest reservationGuest) throws IOException, TemplateException {
        ReservationGuest savedGuest = null;
        savedGuest = repo.save(reservationGuest);
        sendEmaiNotification(savedGuest);
        return savedGuest;
    }

    private void sendEmaiNotification(ReservationGuest savedGuest) throws IOException, TemplateException {
        Map model = new HashMap();
        model.put("guest", savedGuest.getGuest());
        model.put("reservation", savedGuest.getReservation());

        Configuration cfg;
        cfg = configureTemplate(savedGuest);
        Template template = cfg.getTemplate("register.ftl");
//        Template template = configuration.getTemplate("register.ftl");

        StringWriter writer = new StringWriter();
        template.process(model, writer);

        String templateMessage = writer.toString();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        SendMailSSL.sendMail("", templateMessage);
                    }
                }
        ).start();
    }

    private Configuration configureTemplate(ReservationGuest reservationGuest) throws IOException {
        Configuration cfg = new Configuration();

        cfg.setClassForTemplateLoading(ReservationGuest.class, "/templates");
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return cfg;
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
