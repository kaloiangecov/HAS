package has.Guest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kaloi on 12/20/2016.
 */
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    Page<Guest> findByPersonalDataFullNameContainingAndPersonalDataPhoneContaining(String fullName, String phone, Pageable pageRequest);

}
