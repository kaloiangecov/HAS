package has.Utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import has.ReservationGuest.ReservationGuest;
import has.mailsender.SendMailSSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by kaloi on 2/14/2017.
 */
@Component
public class TemplateHandler {

    @Autowired
    private Configuration configuration;

    public void sendMail(Map model, String templatePath, ReservationGuest reservationGuest) throws IOException, TemplateException {

        Template template = configuration.getTemplate("register.ftl");

        StringWriter writer = new StringWriter();
        template.process(model, writer);

        String templateMessage = writer.toString();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        SendMailSSL.sendMail(reservationGuest.getGuest().getUser().getEmail(), templateMessage);
                    }
                }
        ).start();
    }

    public TemplateHandler() {

    }
}
