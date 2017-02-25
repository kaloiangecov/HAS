package has.MealType.Meal;

import has.Meal.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gundev on 19/02/2017.
 */
@Repository
public interface MealCategoryRepository extends JpaRepository<MealCategory, Long>{

    @Query("select m from has.Meal.Meal m where m.mealCategory.id=:id")
    List<Meal> findMealByCategory(@Param("id") Long id);
}
