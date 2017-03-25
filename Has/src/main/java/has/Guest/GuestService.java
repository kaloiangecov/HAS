package has.Guest;

import has.Exceptions.EmailAlreadyExists;
import has.User.User;
import has.User.UserRepository;
import has.Utils.Validator;
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

    @Autowired
    private UserRepository repoUser;

    private static final int NEW_RESEVATION = -1;

    public Guest save(Guest guest) throws Exception {
        validateEgn(guest);
        validateIssueDate(guest);
        validateIdentityNumber(guest);
        validateUserEmail(guest);
        return repo.save(guest);
    }

    public List<Guest> getAllGuests() {
        return repo.findAll();
    }

    public Page<Guest> searchGuests(int start, int length, String sortColumn, String sortDirection, String fullName, String phone) {
        PageRequest request = new PageRequest((start / length), length, Sort.Direction.fromString(sortDirection), sortColumn);
        return repo.findByPersonalDataFullNameContainingIgnoreCaseAndPersonalDataPhoneContaining(fullName, phone, request);
    }

    public List<Guest> findReservationFreeGuests(String startDate, String endDate) {
        return repo.findFreeGuestsForPeriod(startDate, endDate);
    }

    public Guest findById(Long id) throws Exception {
        Guest guest = repo.findOne(id);
        validateIdNotNull(guest);
        return guest;
    }

    public Guest remove(Long id) throws Exception {
        Guest guest = repo.findOne(id);
        validateIdNotNull(guest);
        repo.delete(guest);
        return guest;
    }

    public Guest update(Long id, Guest guest) throws Exception {
        Guest dbGuest = repo.findOne(id);

        validateIssueDate(guest);
        validateEgn(guest);
        validateIdentityNumber(guest);
        validateIdNotNull(dbGuest);

        dbGuest.setNumberReservations(guest.getNumberReservations());
        dbGuest.setStatus(guest.getStatus());
        dbGuest.setPersonalData(guest.getPersonalData());
        dbGuest.setUser(guest.getUser());

        return repo.save(dbGuest);
    }

    public Guest findByUserEmail(String email) throws Exception {
        Guest guest = repo.findByUserEmail(email);
        validateIdNotNull(guest);
        return guest;
    }

    private void validateEgn(Guest guest) throws Exception {
        Guest dbGuest = repo.findByPersonalDataEgn(guest.getPersonalData().getEgn());
        if (dbGuest != null && dbGuest.getId() != guest.getId()) {
            throw new Exception("Guest with EGN " + guest.getPersonalData().getEgn() + " already exists.");
        }
    }

    private void validateIssueDate(Guest guest) throws Exception {
        if (!Validator.isValidIssueExpireDate(guest.getPersonalData().getIdentityIssueDate(), guest.getPersonalData().getIdentityExpireDate())) {
            throw new Exception("Invalid issue date");
        }
    }

    private void validateIdNotNull(Guest guest) throws Exception {
        if (guest == null) {
            throw new Exception("There is no guest with such ID");
        }
    }

    private void validateIdentityNumber(Guest guest) throws Exception {
        Guest dbGuest = repo.findByPersonalDataIdentityNumber(guest.getPersonalData().getIdentityNumber());
        if (dbGuest != null && dbGuest.getId() != guest.getId()) {
            throw new Exception("Guest with Identity Number " + guest.getPersonalData().getIdentityNumber() + " already exists.");
        }
    }

    private void validateUserEmail(Guest guest) throws Exception {
        User dbUser2 = repoUser.findByEmail(guest.getUser().getEmail());
        if (dbUser2 != null && dbUser2.getId() != guest.getUser().getId()) {
            throw new EmailAlreadyExists(guest.getUser().getEmail());
        }
    }
}
