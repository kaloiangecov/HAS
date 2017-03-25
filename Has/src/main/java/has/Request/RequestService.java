package has.Request;

import has.Guest.Guest;
import has.Guest.GuestRepository;
import has.ReservationGuest.ReservationGuestRepository;
import has.User.User;
import has.Utils.TaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Service
public class RequestService {

    @Autowired
    private RequestRepository repo;

    @Autowired
    private GuestRepository guestRepo;

    @Autowired
    private ReservationGuestRepository rgRepo;

    @Autowired
    private TaskHandler taskHandler;

    public Request save(Request request, User user) {
        Guest guest = guestRepo.findByUserId(user.getId());
        if (user.getUserRole().getId() == 5) {
            request.setReservationGuest(rgRepo.findByReservationStatusAndGuestId(1, guest.getId()));
        }
        //taskHandler.createTaskFromRequest(request);
        return repo.save(request);
    }

    public List<Request> getAllRequests() {
        List<Request> requests = repo.findAll();
        return requests;
    }

    public Request findById(Long id) throws Exception {
        Request request = repo.findOne(id);
        validateIdNotNull(request);
        return request;
    }

    public Request remove(Long id) throws Exception {
        Request request = repo.findOne(id);
        validateIdNotNull(request);
        repo.delete(request);
        return request;
    }

    public Request update(Long id, Request request) throws Exception {
        Request dbRequest = repo.findOne(id);
        validateIdNotNull(dbRequest);

        dbRequest.setStatus(request.getStatus());
        dbRequest.setType(request.getType());
        dbRequest.setTargetTime(request.getTargetTime());
        return repo.save(dbRequest);
    }

    private void validateIdNotNull(Request request) throws Exception {
        if (request == null) {
            throw new Exception("There is no request with such ID");
        }
    }
}
