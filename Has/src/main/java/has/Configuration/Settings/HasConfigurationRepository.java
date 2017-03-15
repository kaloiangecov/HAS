package has.Configuration.Settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kaloi on 3/15/2017.
 */
@Repository
public interface HasConfigurationRepository extends JpaRepository<HasConfiguration, Long> {

}
