package has.RequestMeal;

import has.Reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 2/24/2017.
 */
@Service
public class RequestMealService {

    @Autowired
    private RequestMealRepository repo;

    @Autowired
    private ReservationService reservationService;

    public RequestMeal save(RequestMeal requestMeal) {
        return repo.save(requestMeal);
    }

    public List<RequestMeal> getAllRequests() {
        return repo.findAll();
    }

    public RequestMeal findById(Long id) throws Exception {
        RequestMeal requestMeal = repo.findOne(id);
        validateIdNotNull(requestMeal);
        return requestMeal;
    }

    public RequestMeal remove(Long id) throws Exception {
        RequestMeal requestMeal = repo.findOne(id);
        validateIdNotNull(requestMeal);
        repo.delete(requestMeal);
        return requestMeal;
    }

    public RequestMeal update(Long id, RequestMeal requestMeal) throws Exception {
        RequestMeal dbRequestMeal = repo.findOne(id);
        validateIdNotNull(dbRequestMeal);
        dbRequestMeal.setQuantity(requestMeal.getQuantity());
        return repo.save(dbRequestMeal);
    }

    public List<RequestMeal> findGuestExpenses(Long guestId) {
        return repo.findByRequestReservationGuestGuestId(guestId);
    }

    public List<RequestMeal> findReservationExpenses(Long reservationId) {
        return repo.findByRequestReservationGuestReservationId(reservationId);
    }

    public List<RequestMeal> findGroupReservationExpenses(Long reservationGroupId) {
        return repo.findByRequestReservationGuestReservationGroupId(reservationGroupId);
    }

    private void validateIdNotNull(RequestMeal requestMeal) throws Exception {
        if (requestMeal == null) {
            throw new Exception("There is no request with such ID");
        }
    }
}
