package has.PersonalData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kaloi on 1/2/2017.
 */
@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {

    PersonalData findByEgn(String egn);
}
