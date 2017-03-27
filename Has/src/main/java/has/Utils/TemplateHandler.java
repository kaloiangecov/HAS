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
        if (reservation.getReservationGuests() != null && reservation.getReservationGuests().size() > 0) {
            ReservationGuest reservationGuest = reservation.getReservationGuests().get(FIRST);
            Map model = new HashMap();
            String message = "Your reservation has been updated.";
            model.put("headerMessage", "Your reservation has been updated.");
            model.put("reservation", reservation);
            model.put("guest", reservationGuest.getGuest());

            String templatePath = "register.ftl";
            sendMail(model, templatePath, reservationGuest);
        }
    }

    public void sendReservationCode(Reservation reservation) throws IOException, TemplateException {
        ReservationGuest reservationGuest = reservation.getReservationGuests().get(FIRST);
        Map model = new HashMap();
        model.put("reservation", reservation);
        model.put("guest", reservationGuest.getGuest());

        String templatePath = "roomCode.ftl";
        sendMail(model, templatePath, reservationGuest);
    }

    public void sendEmailNotification(ReservationGuest reservationGuest) throws IOException, TemplateException {
        Map model = new HashMap();
        String message = "Thank you for registering in our hotel.";
        model.put("headerMessage", "Thank you for registering in our hotel.");
        model.put("guest", reservationGuest.getGuest());
        model.put("reservation", reservationGuest.getReservation());
        String templatePath = "";
        templatePath = "register.ftl";

        sendMail(model, templatePath, reservationGuest);
    }

    public TemplateHandler() {

    }
}
