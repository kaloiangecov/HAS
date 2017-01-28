package has.Guest;

import has.mailsender.MailTemplates;
import has.mailsender.SendMailSSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Service
public class GuestService {

    @Autowired
    private GuestRepository repo;

    public Guest save(Guest guest) throws Exception {
        Guest dbGuest = repo.findByPersonalDataEgn(guest.getPersonalData().getEgn());
        if (dbGuest != null)
            throw new Exception("Guest with EGN " + guest.getPersonalData().getEgn() + " already exists.");

        new Thread(
                new Runnable() {
                    public void run() {
                        SendMailSSL.sendMail("shit@hotmail.com", MailTemplates.RESERVATION_CONFIRMATION);
                    }
                }).start();
        return repo.save(guest);
    }

    public List<Guest> getAllGuests() {
        return repo.findAll();
    }

    public Page<Guest> searchGuests(int start, int length, String fullName, String phone) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.ASC, "id");
        return repo.findByPersonalDataFullNameContainingAndPersonalDataPhoneContaining(fullName, phone, request);
    }

    public Guest findById(Long id) throws Exception {
        Guest dbGuest = repo.findOne(id);
        if(dbGuest == null){
            throw new Exception("There is no guest with such ID");
        }
        return dbGuest;
    }

    public Guest remove(Long id) throws Exception {
        Guest dbGuest = repo.findOne(id);
        if(dbGuest == null){
            throw new Exception("There is no guest with such ID");
        }
        repo.delete(dbGuest);
        return dbGuest;
    }

    public Guest update(Long id, Guest guest) throws Exception {
        Guest dbGuest = repo.findOne(id);
        if(dbGuest == null){
            throw new Exception("There is no guest with such ID");
        }

        dbGuest.setNumberReservations(guest.getNumberReservations());
        dbGuest.setStatus(guest.getStatus());
        dbGuest.setPersonalData(guest.getPersonalData());
        if (dbGuest.getUser().getId() != guest.getUser().getId())
            dbGuest.setUser(guest.getUser());

        SendMailSSL.sendMail("shit@hotmail.com", MailTemplates.RESERVATION_CONFIRMATION);
        return repo.save(dbGuest);
    }
}
