package has.Configuration.Settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 3/15/2017.
 */
@Service
public class HasConfigurationService {

    @Autowired
    private HasConfigurationRepository repo;

    public HasConfiguration findById(Long id) throws Exception {
        HasConfiguration hasConfiguration = repo.findOne(id);
        validateIdNotNull(hasConfiguration);
        return hasConfiguration;
    }

    public List<HasConfiguration> getAllConfigurations() {
        return repo.findAll();
    }

    public HasConfiguration save(HasConfiguration hasConfiguration) {
        return repo.save(hasConfiguration);
    }

    public HasConfiguration update(Long id, HasConfiguration hasConfiguration) throws Exception {
        HasConfiguration dbHasConfiguration = repo.findOne(id);
        validateIdNotNull(dbHasConfiguration);

        dbHasConfiguration.setAllInclusivePrice(hasConfiguration.getAllInclusivePrice());
        dbHasConfiguration.setBreakfastPrice(hasConfiguration.getBreakfastPrice());
        dbHasConfiguration.setDinnerPrice(hasConfiguration.getDinnerPrice());
        dbHasConfiguration.setDoubleBedPrice(hasConfiguration.getDoubleBedPrice());
        dbHasConfiguration.setGuestDiscount(hasConfiguration.getGuestDiscount());
        dbHasConfiguration.setHotelName(hasConfiguration.getHotelName());
        dbHasConfiguration.setOvernightPrice(hasConfiguration.getOvernightPrice());
        dbHasConfiguration.setSeasonalDiscount(hasConfiguration.getSeasonalDiscount());
        dbHasConfiguration.setSingleBedPrice(hasConfiguration.getSingleBedPrice());

        return repo.save(dbHasConfiguration);
    }

    public HasConfiguration remove(Long id) throws Exception {
        HasConfiguration hasConfiguration = repo.findOne(id);
        validateIdNotNull(hasConfiguration);
        repo.delete(hasConfiguration);
        return hasConfiguration;
    }

    private void validateIdNotNull(HasConfiguration hasConfiguration) throws Exception {
        if (hasConfiguration == null) {
            throw new Exception("There is no configuration with such ID");
        }
    }
}
