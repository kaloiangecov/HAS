package has.Guest;

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
        if (dbGuest != null) {
            throw new Exception("Guest with EGN " + guest.getPersonalData().getEgn() + " already exists.");
        }

                        SendMailSSL.sendMail("shit@hotmail.com", "" );

        return repo.save(guest);
    }

    public List<Guest> getAllGuests() {
        return repo.findAll();
    }

    public Page<Guest> searchGuests(int start, int length, String sortColumn, String sortDirection, String fullName, String phone) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.fromString(sortDirection), sortColumn);
        return repo.findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContaining(fullName, phone, request);
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

        Guest dbGuest2 = repo.findByPersonalDataEgn(guest.getPersonalData().getEgn());
        if (dbGuest2 != null && dbGuest2.getId() != guest.getId())
            throw new Exception("Employee with EGN " + guest.getPersonalData().getEgn() + " already exists.");

        dbGuest.setNumberReservations(guest.getNumberReservations());
        dbGuest.setStatus(guest.getStatus());
        dbGuest.setPersonalData(guest.getPersonalData());
        dbGuest.setUser(guest.getUser());

        SendMailSSL.sendMail("gunesh.shefkedov@gmail.com", "");
        return repo.save(dbGuest);
    }
}
