package has.RequestMeal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kaloi on 2/24/2017.
 */
@Repository
public interface RequestMealRepository extends JpaRepository<RequestMeal, Long> {

    List<RequestMeal> findByRequestReservationGuestGuestId(Long guestId);

    List<RequestMeal> findByRequestReservationGuestReservationId(Long reservationId);

    List<RequestMeal> findByRequestReservationGuestReservationGroupId(Long groupId);
}
