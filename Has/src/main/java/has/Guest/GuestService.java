package has.Guest;

import has.mailsender.SendMailSSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        if (dbGuest != null) {
            throw new Exception("Guest with EGN " + guest.getPersonalData().getEgn() + " already exists.");
        }
        if (!isValid(guest.getPersonalData().getIdentityIssueDate(), guest.getPersonalData().getIdentityExpireDate())){
            throw new Exception("Invalid issue date");
        }

        SendMailSSL.sendMail("shit@hotmail.com", "" );

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
        if (!isValid(guest.getPersonalData().getIdentityIssueDate(), guest.getPersonalData().getIdentityExpireDate())){
            throw new Exception("Invalid issue date");
        }
        Guest dbGuest2 = repo.findByPersonalDataEgn(guest.getPersonalData().getEgn());
        if (dbGuest2 != null && dbGuest2.getId() != guest.getId())
            throw new Exception("Employee with EGN " + guest.getPersonalData().getEgn() + " already exists.");

        dbGuest.setNumberReservations(guest.getNumberReservations());
        dbGuest.setStatus(guest.getStatus());
        dbGuest.setPersonalData(guest.getPersonalData());
        dbGuest.setUser(guest.getUser());

        SendMailSSL.sendMail("asdffgdf@423.com", "");
        return repo.save(dbGuest);
    }

    public boolean isValid(String issueDate, String expirationDate) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date issue = format.parse(issueDate);
        Date expiration = format.parse(expirationDate);
        if(issue.after(expiration)){
            return false;
        }
        if(issue.getTime() > new Date().getTime()){
            return false;
        }
        return true;
    }
}
