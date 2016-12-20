package has.Meal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kaloi on 12/20/2016.
 */
@Repository
public interface MealRepository extends JpaRepository<Meal, Long>{

}
