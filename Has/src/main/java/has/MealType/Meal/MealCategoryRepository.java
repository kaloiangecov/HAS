package has.MealType.Meal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gundev on 19/02/2017.
 */
@Repository
public interface MealCategoryRepository extends JpaRepository<MealCategory, Long> {

}
