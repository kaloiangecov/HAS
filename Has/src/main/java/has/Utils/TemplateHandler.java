package has.Utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import has.Reservation.Reservation;
import has.ReservationGuest.ReservationGuest;
import has.mailsender.SendMailSSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaloi on 2/14/2017.
 */
@Component
public class TemplateHandler {

    private static final int RESERVATION_STATUS_CREATED = 0;
    private static final int RESERVATION_STATUS_ARRIVED = 1;
    private static final int RESERVATION_STATUS_CLOSED = 2;
    private static final int FIRST = 0;

    @Autowired
    private Configuration configuration;

    public void sendMail(Map model, String templatePath, ReservationGuest reservationGuest) throws IOException, TemplateException {

        Template template = configuration.getTemplate(templatePath);

        StringWriter writer = new StringWriter();
        template.process(model, writer);

        String templateMessage = writer.toString();

        if (reservationGuest.getGuest().getUser() != null) {
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            SendMailSSL.sendMail(reservationGuest.getGuest().getUser().getEmail(), templateMessage);
                        }
                    }
            ).start();
        }
    }

    public void notifyCustomer(Reservation reservation) throws IOException, TemplateException {
        ReservationGuest reservationGuest = reservation.getReservationGuests().get(FIRST);
        Map model = new HashMap();
        String message = "Your reservation has been updated.";
        model.put("message", message);
        model.put("reservation", reservation);
        model.put("guest", reservationGuest.getGuest());
        if (reservation.getStatus() == RESERVATION_STATUS_ARRIVED) {

            String templatePath = "roomCode.ftl";
            sendMail(model, templatePath, reservationGuest);
        }
    }

    public TemplateHandler() {

    }
}
