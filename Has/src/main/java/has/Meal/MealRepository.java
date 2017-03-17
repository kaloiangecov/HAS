package has.Meal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kaloi on 12/20/2016.
 */
@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    Page<Meal> findByNameContaining(String name, Pageable request);

    List<Meal> findByMealCategoryId(Long id);
}
