package has.Guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Service
public class GuestService {

    @Autowired
    private GuestRepository repo;

    public Guest save(Guest guest) {
        return repo.save(guest);
    }

    public List<Guest> getAllGuests() {
        return repo.findAll();
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

        return repo.save(dbGuest);
    }
}
