package has.Request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long>{
    List<Request> findByReservationGuestReservationId(Long id);

}
