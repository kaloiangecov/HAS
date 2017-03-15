package has.Configuration.Settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kaloi on 3/15/2017.
 */
@Service
public class HasConfigurationService {

    @Autowired
    private HasConfigurationRepository hasConfigurationRepository;
}
